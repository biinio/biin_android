package com.biin.biin;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Adapters.BNCategoryAdapter;
import com.biin.biin.Adapters.BNHighlightAdapter;
import com.biin.biin.Adapters.BNSiteAdapter;
import com.biin.biin.Components.CardRecyclerView;
import com.biin.biin.Components.LinearLayoutManagerSmooth;
import com.biin.biin.Components.Listeners.IBNLoadMoreElementsListener;
import com.biin.biin.Components.Listeners.BNMoreElementsListener;
import com.biin.biin.Components.Listeners.IBNSitesLikeListener;
import com.biin.biin.Components.Listeners.HighlightsPagerListener;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Entities.BiinieAction;
import com.biin.biin.Managers.BNAnalyticsManager;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Utils.BeaconSort;
import com.biin.biin.Volley.Listeners.BNElementsListener;
import com.biin.biin.Volley.Listeners.BNInitialDataListener;
import com.jude.rollviewpager.RollPagerView;

import com.kontakt.sdk.android.ble.configuration.scan.ScanMode;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements HighlightsPagerListener.IBNHighlightsListener, IBNSitesLikeListener, BNInitialDataListener.IBNInitialDataListener {

    private static final String TAG = "MainActivity";

    private TextView tvNearYou, tvFavouritePlaces;
    private TextView tvProfile, tvFavourites, tvFriends, tvAbout;
    private CardRecyclerView rvNearSites, rvFavouritePlaces;
    private LinearLayout hlRecomended;
    private RelativeLayout rlCloseApp;
    private DrawerLayout drawer;

    private Biinie biinie;
    private BNDataManager dataManager;
    private BNAnalyticsManager analyticsManager;
    private BroadcastReceiver locationsReceiver;
    private BNInitialDataListener initialDataListener;
    private ProximityManagerContract proximityManager;

    private List<BNSite> nearSites;
    private List<BNSite> favoriteSites;
    private BNSiteAdapter nearPlacesAdapter;
    private BNSiteAdapter favoritesAdapter;

    private int nearBySitesVersion;
    private int favouriteSitesVersion;

    private long animDuration = 300;
    private boolean loaded = false;

    private int beaconMajor;
    private double beaconDistance;

    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        BNUtils.setWidth(metrics.widthPixels);
        BNUtils.setDensity(metrics.density);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();
        analyticsManager = BNAppManager.getInstance().getAnalyticsManagerInstance();

        biinie = dataManager.getBiinie();

        setUpScreen();
        loadData();
        startServices();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();
        Typeface lato_light = BNUtils.getLato_light();

        TextView tvRecomended = (TextView) findViewById(R.id.tvRecomended);
        tvRecomended.setTypeface(lato_regular);
        tvRecomended.setLetterSpacing(0.3f);

        tvNearYou = (TextView) findViewById(R.id.tvOtherNearYou);
        tvNearYou.setTypeface(lato_regular);
        tvNearYou.setLetterSpacing(0.3f);

        TextView tvEmptyNearBy = (TextView) findViewById(R.id.tvEmptyNearBy);
        tvEmptyNearBy.setTypeface(lato_regular);
        tvEmptyNearBy.setLetterSpacing(0.3f);

        tvFavouritePlaces = (TextView) findViewById(R.id.tvFavouritePlaces);
        tvFavouritePlaces.setTypeface(lato_regular);
        tvFavouritePlaces.setLetterSpacing(0.3f);

        TextView tvEmptyFavourites = (TextView) findViewById(R.id.tvEmptyFavourites);
        tvEmptyFavourites.setTypeface(lato_regular);
        tvEmptyFavourites.setLetterSpacing(0.3f);

        hlRecomended = (LinearLayout) findViewById(R.id.hlRecomended);
        rlCloseApp = (RelativeLayout) findViewById(R.id.rlCloseApp);
        rlCloseApp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        TextView tvCloseApp = (TextView) findViewById(R.id.tvCloseApp);
        tvCloseApp.setTypeface(lato_regular);
        tvCloseApp.setLetterSpacing(0.3f);

        TextView tvConfirmClose = (TextView) findViewById(R.id.tvConfirmClose);
        tvConfirmClose.setTypeface(lato_regular);
        tvConfirmClose.setLetterSpacing(0.3f);
        tvConfirmClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyticsManager.addAction(new BiinieAction("", BiinieAction.CLOSE_APP, BiinieAction.AndroidApp));
                MainActivity.super.onBackPressed();
            }
        });

        TextView tvDontClose = (TextView) findViewById(R.id.tvDontClose);
        tvDontClose.setTypeface(lato_regular);
        tvDontClose.setLetterSpacing(0.3f);
        tvDontClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlCloseApp.setVisibility(View.GONE);
            }
        });

        int height = (BNUtils.getWidth() / 2) + (int) (56 * BNUtils.getDensity());

        rvNearSites = (CardRecyclerView) findViewById(R.id.rvOtherNearYou);
        rvFavouritePlaces = (CardRecyclerView) findViewById(R.id.rvFavouritePlaces);
        ViewGroup.LayoutParams nearSitesParams = rvNearSites.getLayoutParams();
        ViewGroup.LayoutParams favsSitesParams = rvFavouritePlaces.getLayoutParams();
        nearSitesParams.height = height;
        favsSitesParams.height = height;
        rvNearSites.setLayoutParams(nearSitesParams);
        rvFavouritePlaces.setLayoutParams(favsSitesParams);

        TextView tvTitle, tvSubtitle, tvLocation, tvBluetooth, tvMessage1, tvMessage2;
        tvTitle = (TextView)findViewById(R.id.tvBeaconTitle);
        tvSubtitle = (TextView)findViewById(R.id.tvBeaconSubtitle);
        tvLocation = (TextView)findViewById(R.id.tvBeaconLocation);
        tvBluetooth = (TextView)findViewById(R.id.tvBluetoothTitle);
        tvMessage1 = (TextView)findViewById(R.id.tvBluetoothMessage1);
        tvMessage2 = (TextView)findViewById(R.id.tvBluetoothMessage2);

        tvTitle.setTypeface(lato_black);
        tvLocation.setTypeface(lato_regular);
        tvSubtitle.setTypeface(lato_light);
        tvBluetooth.setTypeface(lato_black);
        tvMessage1.setTypeface(lato_regular);
        tvMessage2.setTypeface(lato_regular);

        setUpDrawer();
    }

    private void setUpDrawer() {
        Typeface lato_regular = BNUtils.getLato_regular();
        drawer = (DrawerLayout) findViewById(R.id.dlMain);

        tvProfile = (TextView) findViewById(R.id.tvMenuProfile);
        tvProfile.setTypeface(lato_regular);
        tvProfile.setLetterSpacing(0.3f);
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        tvFavourites = (TextView) findViewById(R.id.tvMenuFavourites);
        tvFavourites.setTypeface(lato_regular);
        tvFavourites.setLetterSpacing(0.3f);
        tvFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ElementsListActivity.class);
                SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BNUtils.BNStringExtras.BNCategory, BNUtils.BNStringExtras.BNFavorites);
                editor.commit();

                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        tvFriends = (TextView) findViewById(R.id.tvMenuFriends);
        tvFriends.setTypeface(lato_regular);
        tvFriends.setLetterSpacing(0.3f);
        tvFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_link));
                i.setType("text/plain");
                startActivity(Intent.createChooser(i, getString(R.string.ActionShare)));
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        tvAbout = (TextView) findViewById(R.id.tvMenuAbout);
        tvAbout.setTypeface(lato_regular);
        tvAbout.setLetterSpacing(0.3f);
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        ImageView ivMenu = (ImageView) findViewById(R.id.ivToolbarMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        RelativeLayout rlDrawer = (RelativeLayout) findViewById(R.id.bnNavView);
        rlDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void loadData() {
        nearBySitesVersion = dataManager.getNearBySitesVersion();
        favouriteSitesVersion = dataManager.getFavouriteSitesVersion();

        loadRecomendations();
        loadFavourites();
        loadNearPlaces();
        loadCategories();
    }

    private void startServices(){
        proximityManager = new ProximityManager(this);
        proximityManager.configuration().scanMode(ScanMode.BALANCED);
        proximityManager.configuration().deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(30));
        proximityManager.setIBeaconListener(createIBeaconListener());

        analyticsManager.addAction(new BiinieAction("", BiinieAction.OPEN_APP, BiinieAction.AndroidApp));

        Intent i = new Intent(this, LocationService.class);
        startService(i);

        locationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getInitialData();
            }
        };
    }

    private void loadRecomendations() {
        LinkedHashMap<String, BNElement> allElements = dataManager.getBNElements();
        List<BNHighlight> highlights = dataManager.getBNHighlights();
        List<BNElement> elements = new ArrayList<>();

        for (BNHighlight highlight : highlights) {
            BNElement element = allElements.get(highlight.getIdentifier());
            BNShowcase showcase = dataManager.getBNShowcase(highlight.getShowcaseIdentifier());
            BNSite site = dataManager.getBNSite(highlight.getSiteIdentifier());

            if (element != null && showcase != null && site != null) {
                showcase.setSite(site);
                element.setShowcase(showcase);
                elements.add(element);
            } else {
                Log.e(TAG, "No se encontraron los datos completos del elemento (element, showcase y site)");
            }
        }
        total = elements.size();

        if (total > 0) {
            HighlightsPagerListener listener = new HighlightsPagerListener(this);
            listener.setLength(total);
//            setPaggingDots(total);

            RollPagerView pvHighlights = (RollPagerView) findViewById(R.id.pvRecomended);
            pvHighlights.setAnimationDurtion(1000);
            pvHighlights.setHintView(null);
            pvHighlights.getViewPager().addOnPageChangeListener(listener);
            BNHighlightAdapter adapter = new BNHighlightAdapter(pvHighlights, this);
            adapter.setHighlights(elements);
            pvHighlights.setAdapter(adapter);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(BNUtils.getWidth(), BNUtils.getWidth() + (int) (68 * BNUtils.getDensity()));
            params.addRule(RelativeLayout.BELOW, R.id.hlRecomended);
            pvHighlights.setLayoutParams(params);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("LOCATION_SERVICE");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(locationsReceiver, filter);

        Intent i = new Intent(this, BeaconsService.class);
        stopService(i);
        startScanning();
    }

    @Override
    protected void onStop() {
        loaded = false;
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(locationsReceiver);
        proximityManager.stopScanning();
        Intent i = new Intent(this, BeaconsService.class);
        startService(i);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int nearVersion = dataManager.getNearBySitesVersion();
        int favsVersion = dataManager.getFavouriteSitesVersion();

        if (nearVersion > nearBySitesVersion) {
            nearBySitesVersion = nearVersion;
            nearSites = dataManager.getNearByBNSites();
            int size = nearSites.size();
            nearPlacesAdapter.notifyItemRangeChanged(0, size);
            if(size > 0){
                showNearPlacesEmpty(false);
            }else{
                showNearPlacesEmpty(true);
            }
        }

        if (favsVersion > favouriteSitesVersion) {
            favouriteSitesVersion = favsVersion;
            favoriteSites = dataManager.getFavouriteBNSites();
            int size = favoriteSites.size();
            favoritesAdapter.notifyItemRangeChanged(0, size);
            if(size > 0){
                showFavouritesEmpty(false);
            }else{
                showFavouritesEmpty(true);
            }
        }
        loaded = true;
    }

    @Override
    public void onPageSelected(int position) {
        setPaggingDots(position);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (rlCloseApp.getVisibility() == View.GONE) {
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
        }
        return false;
    }

    private void setPaggingDots(int position) {
        float density = BNUtils.getDensity();
        hlRecomended.removeAllViews();
        for (int i = 0; i < total; i++) {
            if (i == position) {
                ImageView dotBig = new ImageView(this);
                dotBig.setImageResource(R.drawable.pagging_dot);
                dotBig.setColorFilter(getResources().getColor(R.color.colorAccentGray));
                dotBig.setLayoutParams(new LinearLayout.LayoutParams((int) (20 * density), (int) (9 * density)));
                hlRecomended.addView(dotBig);
            } else {
                ImageView dotSmall = new ImageView(this);
                dotSmall.setImageResource(R.drawable.pagging_dot);
                dotSmall.setColorFilter(getResources().getColor(R.color.colorPrimary));
                dotSmall.setLayoutParams(new LinearLayout.LayoutParams((int) (20 * density), (int) (7 * density)));
                hlRecomended.addView(dotSmall);
            }
        }
    }

    private void loadNearPlaces() {
        showNearPlacesList();
        if (nearSites.size() > 0) {
            showNearPlacesEmpty(false);
        } else {
            showNearPlacesEmpty(true);
        }
    }

    private void showNearPlacesList() {
        LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.HORIZONTAL, false);

        nearSites = dataManager.getNearByBNSites();
        nearPlacesAdapter = new BNSiteAdapter(this, nearSites, this, rvNearSites, false);
        nearPlacesAdapter.setShowOthers(true);
        rvNearSites.setLayoutManager(layoutManager);
        rvNearSites.setHasFixedSize(true);
        rvNearSites.setAdapter(nearPlacesAdapter);

        findViewById(R.id.ivOtherNearYou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SitesListActivity.class);
                i.putExtra(BNUtils.BNStringExtras.BNFavorites, false);
                MainActivity.this.startActivity(i);
            }
        });
    }

    private void showNearPlacesEmpty(boolean empty) {
        CardRecyclerView rvNearYou = (CardRecyclerView) findViewById(R.id.rvOtherNearYou);
        ImageView ivNearYou = (ImageView) findViewById(R.id.ivOtherNearYou);
        LinearLayout vlNearYou = (LinearLayout) findViewById(R.id.vlEmptyNearPlaces);

        if (empty) {
            tvNearYou.setVisibility(View.INVISIBLE);
            ivNearYou.setVisibility(View.GONE);
            rvNearYou.setVisibility(View.INVISIBLE);
            vlNearYou.setVisibility(View.VISIBLE);
        } else {
            tvNearYou.setVisibility(View.VISIBLE);
            ivNearYou.setVisibility(View.VISIBLE);
            rvNearYou.setVisibility(View.VISIBLE);
            vlNearYou.setVisibility(View.INVISIBLE);
        }
    }

    private void loadFavourites() {
        showFavouritesList();
        if (favoriteSites.size() > 0) {
            showFavouritesEmpty(false);
        } else {
            showFavouritesEmpty(true);
        }
    }

    private void showFavouritesList() {
        ImageView ivFavouritePlaces = (ImageView) findViewById(R.id.ivFavouritePlaces);
        LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.HORIZONTAL, false);

        favoriteSites = dataManager.getFavouriteBNSites();
        favoritesAdapter = new BNSiteAdapter(this, favoriteSites, this, rvFavouritePlaces, true);
        favoritesAdapter.setShowOthers(true);
        rvFavouritePlaces.setLayoutManager(layoutManager);
        rvFavouritePlaces.setHasFixedSize(true);
        rvFavouritePlaces.setAdapter(favoritesAdapter);

        ivFavouritePlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SitesListActivity.class);
                i.putExtra(BNUtils.BNStringExtras.BNFavorites, true);
                MainActivity.this.startActivity(i);
            }
        });
    }

    private void showFavouritesEmpty(boolean empty) {
        CardRecyclerView rvFavouritePlaces = (CardRecyclerView) findViewById(R.id.rvFavouritePlaces);
        ImageView ivFavouritePlaces = (ImageView) findViewById(R.id.ivFavouritePlaces);
        LinearLayout vlFavourites = (LinearLayout) findViewById(R.id.vlEmptyFavorites);

        if (empty) {
            tvFavouritePlaces.setVisibility(View.INVISIBLE);
            ivFavouritePlaces.setVisibility(View.GONE);
            rvFavouritePlaces.setVisibility(View.INVISIBLE);
            vlFavourites.setVisibility(View.VISIBLE);
        } else {
            tvFavouritePlaces.setVisibility(View.VISIBLE);
            ivFavouritePlaces.setVisibility(View.VISIBLE);
            rvFavouritePlaces.setVisibility(View.VISIBLE);
            vlFavourites.setVisibility(View.INVISIBLE);
        }
    }

    private void loadCategories() {
        Typeface lato_regular = BNUtils.getLato_regular();
        // layout al que se va a agregar las listas de categorias
        LinearLayout layout = (LinearLayout) findViewById(R.id.vlShowcases);

        // obtener la lista de categorias
        List<BNCategory> categories = new ArrayList<>(dataManager.getBNCategories().values());
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        for (final BNCategory category : categories) {
            // obtener la lista de elementos de cada categoria
            List<BNElement> categoryElements = category.getElements();
            if (categoryElements.size() > 0) {
                // poner el label (titulo) y setear el typeface
                View view = inflater.inflate(R.layout.bncategory_list, null);
                TextView tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
                ImageView ivCategoryName = (ImageView) view.findViewById(R.id.ivCategoryName);

                tvCategoryName.setTypeface(lato_regular);
                tvCategoryName.setLetterSpacing(0.3f);
                tvCategoryName.setText(getResources().getIdentifier(category.getIdentifier(), "string", getPackageName()));

                // llenar el recyclerview
                CardRecyclerView rvCategoryList = (CardRecyclerView) view.findViewById(R.id.rvCategoryList);
//                rvCategoryList.setSnapEnabled(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                final BNCategoryAdapter adapter = new BNCategoryAdapter(this, categoryElements, rvCategoryList);
                adapter.setOnLoadMoreListener(new IBNLoadMoreElementsListener() {
                    @Override
                    public void onLoadMoreElements() {
                        category.getElements().add(null);
                        adapter.notifyItemInserted(category.getElements().size() - 1);

                        BNElementsListener elementsListener = new BNElementsListener(category.getElements(), false);
                        elementsListener.setListener(new BNMoreElementsListener(adapter));

                        String url = BNAppManager.getInstance().getNetworkManagerInstance().getMoreCategoryElementsUrl(biinie.getIdentifier(), category.getIdentifier());
                        Log.d(TAG, url);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.GET,
                                url,
                                null,
                                elementsListener,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(TAG, "Error:" + error.getMessage());
                                        adapter.setLoaded();
                                    }
                                });
                        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "MoreElementsData");
                    }
                });
                rvCategoryList.setLayoutManager(layoutManager);
                rvCategoryList.setHasFixedSize(true);
                rvCategoryList.setAdapter(adapter);

                // accion de ver mas
                ivCategoryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, ElementsListActivity.class);
                        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(BNUtils.BNStringExtras.BNCategory, category.getIdentifier());
                        editor.commit();
                        MainActivity.this.startActivity(i);
                    }
                });

                // attach del view a la pantalla principal
                layout.addView(view);
            }
        }
    }

    private void startScanning() {
        BluetoothAdapter bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothadapter.isEnabled()) { //bluetooth active
            proximityManager.connect(new OnServiceReadyListener() {
                @Override
                public void onServiceReady() {
                    proximityManager.startScanning();
                    Log.e(TAG, "Start scanning");
                }
            });
        }else{
            Log.e(TAG, "Bluetooth apagado");
//            mostrar alerta de que seria bueno encenderlo (?)
            final RelativeLayout rlBluetoothAlert = (RelativeLayout) findViewById(R.id.rlBluetoothAlert);
            ImageView ivBluetoothClose = (ImageView) findViewById(R.id.ivBluetoothClose);
            rlBluetoothAlert.setVisibility(View.VISIBLE);

            rlBluetoothAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(i);
                    rlBluetoothAlert.setVisibility(View.GONE);
                }
            });

            ivBluetoothClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlBluetoothAlert.setVisibility(View.GONE);
                }
            });
        }
    }

    private IBeaconListener createIBeaconListener() {
        return new IBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.e("IBeacon", "IBeacon discovered: " + iBeacon.getUniqueId());
            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                if (iBeacons.size() > 0) {
                    IBeaconDevice nearest = iBeacons.get(0);
                    boolean found = false;

                    for (IBeaconDevice device : iBeacons) {
                        if(device.getMajor() == beaconMajor){
                            beaconDistance = device.getDistance();
                            found = true;
                        }

                        if (device.getDistance() < nearest.getDistance()) {
                            if (dataManager.getBNSiteByMajor(device.getMajor()) != null) {
                                nearest = device;
                                beaconDistance = nearest.getDistance();
                                beaconMajor = nearest.getMajor();
                            }
                        }
                    }

                    if (!found || (nearest.getMajor() != beaconMajor && (nearest.getDistance() + 5d) <  beaconDistance)) {
                        showBeaconAlert(nearest);
                    }
                } else {
                    hideBeaconAlert();
                }
            }

            @Override
            public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.e("IBeacon", "IBeacon lost: " + iBeacon.getUniqueId());
            }
        };
    }

    private void showBeaconAlert(IBeaconDevice device){
        final BNSite beaconSite = dataManager.getBNSiteByMajor(device.getMajor());

        if(beaconSite != null) {
            RelativeLayout rlBeaconAlert = (RelativeLayout) findViewById(R.id.rlBeaconAlert);
            ImageView ivBeaconOrganization = (ImageView) findViewById(R.id.ivBeaconOrganization);
            TextView tvTitle, tvSubtitle, tvLocation;
            tvTitle = (TextView) findViewById(R.id.tvBeaconTitle);
            tvSubtitle = (TextView) findViewById(R.id.tvBeaconSubtitle);
            tvLocation = (TextView) findViewById(R.id.tvBeaconLocation);

            tvTitle.setText(beaconSite.getTitle());
            tvTitle.setTextColor(beaconSite.getOrganization().getSecondaryColor());
            tvLocation.setText(beaconSite.getSubTitle());
            tvLocation.setTextColor(beaconSite.getOrganization().getSecondaryColor());
            tvSubtitle.setText(beaconSite.getNutshell());
            tvSubtitle.setTextColor(beaconSite.getOrganization().getSecondaryColor());

            rlBeaconAlert.setBackgroundColor(beaconSite.getOrganization().getPrimaryColor());
            ImageLoader.getInstance().displayImage(beaconSite.getOrganization().getMedia().get(0).getUrl(), ivBeaconOrganization);

            rlBeaconAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, SitesActivity.class);
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(BNUtils.BNStringExtras.BNSite, beaconSite.getIdentifier());
                    editor.commit();
                    i.putExtra(BNUtils.BNStringExtras.BNShowOthers, false);
                    startActivity(i);
                }
            });
            rlBeaconAlert.setVisibility(View.VISIBLE);
            
        }else{
            Log.e(TAG, "El Site del beacon (major: " + device.getMajor() + ") es null");
            hideBeaconAlert();
        }
    }

    private void hideBeaconAlert(){
        findViewById(R.id.rlBeaconAlert).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tvBeaconTitle)).setText("");
        ((TextView)findViewById(R.id.tvBeaconLocation)).setText("");
        ((TextView)findViewById(R.id.tvBeaconSubtitle)).setText("");
    }

    @Override
    public void onBackPressed() {
        if(loaded) {
            if (rlCloseApp.getVisibility() == View.GONE) {
                rlCloseApp.setVisibility(View.VISIBLE);
            } else {
                rlCloseApp.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSiteLiked(String identifier, final int position) {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.LIKE_SITE, identifier));

        if(dataManager.likeBNSite(identifier)) {

            nearSites = dataManager.getNearByBNSites();
            favoriteSites = dataManager.getFavouriteBNSites();

            favoritesAdapter.notifyItemInserted(0);
            nearPlacesAdapter.notifyItemRemoved(position);

            if (favoriteSites.size() > 2) {
                rvFavouritePlaces.smoothScrollToPosition(0);
            }

            verifyLists();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    favoritesAdapter.notifyItemRangeChanged(0, favoriteSites.size());
                    nearPlacesAdapter.notifyItemRangeChanged(position, nearSites.size());
                }
            }, animDuration + 300);
        }
    }

    @Override
    public void onSiteUnliked(String identifier, final int position) {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.UNLIKE_SITE, identifier));

        if(dataManager.unlikeBNSite(identifier)) {

            nearSites = dataManager.getNearByBNSites();
            favoriteSites = dataManager.getFavouriteBNSites();

            nearPlacesAdapter.notifyItemInserted(0);
            favoritesAdapter.notifyItemRemoved(position);

            if (nearSites.size() > 2) {
                rvNearSites.smoothScrollToPosition(0);
            }

            verifyLists();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    nearPlacesAdapter.notifyItemRangeChanged(0, nearSites.size());
                    favoritesAdapter.notifyItemRangeChanged(position, favoriteSites.size());
                }
            }, animDuration + 300);
        }
    }

    private void verifyLists(){
        if(nearSites.size() > 0){
            showNearPlacesEmpty(false);
        }else{
            showNearPlacesEmpty(true);
        }

        if(favoriteSites.size() > 0){
            showFavouritesEmpty(false);
        }else{
            showFavouritesEmpty(true);
        }
    }

    private void getInitialData(){
        String location = "0/0";
        Location lastLocation = BNAppManager.getInstance().getPositionManagerInstance().getLastLocation();

        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            location = latitude + "/" + longitude;
        } else {
            Log.e(TAG, "No se puede obtener la localizacion. Puede no estar activada en el dispositivo.");
        }

        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();

        initialDataListener = new BNInitialDataListener();
        initialDataListener.setListener(this);

        String url = BNAppManager.getInstance().getNetworkManagerInstance().getUrlInitialData(biinie.getIdentifier(), location);
        Log.d(TAG, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                initialDataListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onInitialDataError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "InitialData");
    }

    @Override
    public void onInitialDataLoaded() {
        Log.e(TAG, "Initial data reloaded");
//        reloadData();
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    private void onInitialDataError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
    }

    private void reloadData() {
        nearBySitesVersion = dataManager.getNearBySitesVersion();
        favouriteSitesVersion = dataManager.getFavouriteSitesVersion();

//        reloadRecomendations();
        loadFavourites();
        loadNearPlaces();
        loadCategories();
    }

    private void reloadRecomendations() {
        LinkedHashMap<String, BNElement> allElements = dataManager.getBNElements();
        List<BNHighlight> highlights = dataManager.getBNHighlights();
        List<BNElement> elements = new ArrayList<>();

        for (BNHighlight highlight : highlights) {
            BNElement element = allElements.get(highlight.getIdentifier());
            BNShowcase showcase = dataManager.getBNShowcase(highlight.getShowcaseIdentifier());
            BNSite site = dataManager.getBNSite(highlight.getSiteIdentifier());

            if (element != null && showcase != null && site != null) {
                showcase.setSite(site);
                element.setShowcase(showcase);
                elements.add(element);
            } else {
                Log.e(TAG, "No se encontraron los datos completos del elemento (element, showcase y site)");
            }
        }
        total = elements.size();

        if (total > 0) {
            HighlightsPagerListener listener = new HighlightsPagerListener(this);
            listener.setLength(total);

            RollPagerView pvHighlights = (RollPagerView) findViewById(R.id.pvRecomended);
            BNHighlightAdapter adapter = new BNHighlightAdapter(pvHighlights, this);
            adapter.setHighlights(elements);
            pvHighlights.setAdapter(adapter);
        }
    }

}































