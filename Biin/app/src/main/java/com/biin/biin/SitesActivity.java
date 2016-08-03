package com.biin.biin;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Components.Listeners.IBNLoadMoreSitesListener;
import com.biin.biin.Components.Listeners.IBNSitesLikeListener;
import com.biin.biin.Components.Listeners.IBNToolbarListener;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Entities.BiinieAction;
import com.biin.biin.Managers.BNAnalyticsManager;
import com.biin.biin.Utils.BNSiteNps;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Utils.BNUtils.BNStringExtras;
import com.biin.biin.Adapters.BNShowcaseAdapter;
import com.biin.biin.Adapters.BNSiteAdapter;
import com.biin.biin.Components.CardRecyclerView;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Volley.Listeners.BNLikesListener;
import com.biin.biin.Volley.Listeners.BNSitesListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SitesActivity extends AppCompatActivity implements IBNSitesLikeListener, BNSitesListener.IBNSitesListener, IBNToolbarListener, BNLikesListener.IBNLikesListener, OnMapReadyCallback {

    private static final String TAG = "SitesActivity";

    private BNSite currentSite;
    private String siteIdentifier;
    private ImageLoader imageLoader;
    private boolean showOthers = false;

    private Biinie biinie;
    private BNDataManager dataManager;
    private BNAnalyticsManager analyticsManager;
    private BNSitesListener sitesListener;
    private BNLikesListener likesListener;
    private BNSiteAdapter adapter;

    private GoogleMap mMap;
    private Animation inAlpha;
    private Animation inAnimation;
    private Animation outAlpha;
    private Animation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();
        analyticsManager = BNAppManager.getInstance().getAnalyticsManagerInstance();
        biinie = dataManager.getBiinie();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        siteIdentifier = preferences.getString(BNStringExtras.BNSite, "site");

        Intent i = getIntent();
        if(i != null){
            showOthers = i.getBooleanExtra(BNUtils.BNStringExtras.BNShowOthers, false);
        }

        setUpScreen();
        loadShowcases();
        if(showOthers) {
            loadNearPlaces();
        }

        likesListener = new BNLikesListener();
        likesListener.setListener(this);

        loadMap();
        loadAnimations();
        setUpButtons();

        analyticsManager.addAction(new BiinieAction("", BiinieAction.ENTER_SITE_VIEW, currentSite.getIdentifier()));

        setUpUber();
    }

    private void setUpUber(){
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("2zDN6GkBgADY9FApz8dPsKK5hXPlmKvD") //This is necessary
                //.setRedirectUri("YOUR_REDIRECT_URI") //This is necessary if you'll be using implicit grant
                .setEnvironment(SessionConfiguration.Environment.SANDBOX) //Useful for testing your app in the sandbox environment
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS)) //Your scopes for authentication here
                .build();
        //This is a convenience method and will set the default config to be used in other components without passing it directly.
        UberSdk.initialize(config);
    }

    private void setUpScreen(){
        imageLoader = ImageLoader.getInstance();
        currentSite = dataManager.getBNSite(siteIdentifier);
        if(currentSite != null) {
            RelativeLayout rlSiteLabel;
            TextView tvTitle, tvSubtitle, tvLocation;
            TextView tvMapOrg, tvMapLoc, tvMapDet, tvMapCity, tvMapPhone, tvMapEmail, tvMapWaze, tvMapClose;
            TextView tvMapUber;
//            RideRequestButton tvMapUber;
            final ImageView ivSite, ivSiteOrganization;

            rlSiteLabel = (RelativeLayout)findViewById(R.id.rlSiteLabel);

            ivSite = (ImageView)findViewById(R.id.ivSite);
            ivSiteOrganization = (ImageView)findViewById(R.id.ivSiteOrganization);

            tvTitle = (TextView)findViewById(R.id.tvSiteTitle);
            tvSubtitle = (TextView)findViewById(R.id.tvSiteSubtitle);
            tvLocation = (TextView)findViewById(R.id.tvSiteLocation);

            tvMapOrg = (TextView)findViewById(R.id.tvMapOrganization);
            tvMapLoc = (TextView)findViewById(R.id.tvMapLocation);
            tvMapDet = (TextView)findViewById(R.id.tvMapDetails);
            tvMapCity = (TextView)findViewById(R.id.tvMapCity);
            tvMapPhone = (TextView)findViewById(R.id.tvMapPhone);
            tvMapEmail = (TextView)findViewById(R.id.tvMapEmail);
            tvMapWaze = (TextView)findViewById(R.id.tvMapWaze);
//            tvMapUber = (RideRequestButton)findViewById(R.id.tvMapUber);
            tvMapUber = (TextView)findViewById(R.id.tvMapUber);
            tvMapClose = (TextView)findViewById(R.id.tvMapClose);

            Typeface lato_light = BNUtils.getLato_light();
            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvTitle.setTypeface(lato_black);
            tvLocation.setTypeface(lato_regular);
            tvSubtitle.setTypeface(lato_light);

            tvMapOrg.setTypeface(lato_black);
            tvMapLoc.setTypeface(lato_regular);
            tvMapDet.setTypeface(lato_light);
            tvMapCity.setTypeface(lato_light);
            tvMapPhone.setTypeface(lato_light);
            tvMapEmail.setTypeface(lato_light);
            tvMapWaze.setTypeface(lato_black);
//            tvMapUber.setTypeface(lato_black);
            tvMapClose.setTypeface(lato_black);

            imageLoader.displayImage(currentSite.getMedia().get(0).getUrl(), ivSite);
            imageLoader.displayImage(currentSite.getOrganization().getMedia().get(0).getUrl(), ivSiteOrganization);

            rlSiteLabel.setBackgroundColor(currentSite.getOrganization().getPrimaryColor());
            tvTitle.setText(currentSite.getTitle());
            tvTitle.setTextColor(currentSite.getOrganization().getSecondaryColor());
            tvLocation.setText(currentSite.getSubTitle());
            tvLocation.setTextColor(currentSite.getOrganization().getSecondaryColor());
            tvSubtitle.setText(currentSite.getNutshell());
            tvSubtitle.setTextColor(currentSite.getOrganization().getSecondaryColor());
            tvMapOrg.setText(currentSite.getTitle() + " - " + currentSite.getSubTitle());
            tvMapLoc.setText(currentSite.getStreetAddress1());
            tvMapDet.setText(currentSite.getStreetAddress2());
            tvMapCity.setText(currentSite.getCountry() + ", " + currentSite.getState());
            tvMapPhone.setText(getResources().getString(R.string.Phone) + ": " + currentSite.getPhoneNumber());
            tvMapEmail.setText(getResources().getString(R.string.Email) + ": " + currentSite.getEmail());

            BNToolbar toolbar = new BNToolbar(this, currentSite.getOrganization().getPrimaryColor(), currentSite.getOrganization().getSecondaryColor(), currentSite.isUserLiked(), true, true, true, true, false);
            toolbar.setListener(this);

            if(currentSite.getOrganization() != null && currentSite.getOrganization().isHasNPS()){
                FrameLayout npsLayout = (FrameLayout)findViewById(R.id.flNpsInclude);
                npsLayout.setVisibility(View.VISIBLE);
                findViewById(R.id.etNpsComments).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){

                        }else{

                        }
                    }
                });

//                new BNSiteNps(this, currentSite, dataManager.isNpsAvailable(currentSite.getIdentifier()));
            }
        }else{
            Log.e(TAG, "No se encontró el site con el identifier " + siteIdentifier);
            finish();
        }
    }

    private void loadShowcases(){
        Typeface lato_regular = BNUtils.getLato_regular();
        // layout al que se va a agregar las listas de categorias
        LinearLayout layout = (LinearLayout)findViewById(R.id.vlSiteShowcases);

        // obtener la lista de showcases
        List<String> showcases = new ArrayList<>(currentSite.getShowcases());
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        for (String showcaseIdentifier : showcases) {
            // obtener el showcase por identifier
            BNShowcase showcase = dataManager.getBNShowcase(showcaseIdentifier);

            // obtener la lista de elementos de cada showcase
            List<String> elementIdentifiers = showcase.getElements();
            List<BNElement> showcaseElements = new ArrayList<>();

            if(elementIdentifiers.size() > 0) {
                for (String elementIdentifier : elementIdentifiers) {
                    BNElement element = dataManager.getBNElement(elementIdentifier);
                    if (element != null) {
                        showcaseElements.add(element);
                    }
                }
            }

            if(showcaseElements.size() > 0) {
                // poner el label (titulo) y setear el typeface
                View view = inflater.inflate(R.layout.bnshowcase_list, null);
                TextView tvShowcaseName = (TextView) view.findViewById(R.id.tvShowcaseName);
                tvShowcaseName.setTypeface(lato_regular);
                tvShowcaseName.setLetterSpacing(0.3f);
                tvShowcaseName.setText(showcase.getTitle());

                // llenar el recyclerview
                CardRecyclerView rvShowcaseList = (CardRecyclerView)view.findViewById(R.id.rvShowcaseList);
//                rvShowcaseList.setSnapEnabled(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                BNShowcaseAdapter adapter = new BNShowcaseAdapter(this, showcaseElements);
                rvShowcaseList.setLayoutManager(layoutManager);
                rvShowcaseList.setHasFixedSize(true);
                rvShowcaseList.setAdapter(adapter);

                // attach del view a la pantalla principal
                layout.addView(view);
            }
        }
    }

    private void loadNearPlaces(){
        List<String> siteIdentifiers = currentSite.getOrganization().getSites();
        siteIdentifiers.remove(currentSite.getIdentifier());

        if(siteIdentifiers.size() > 0) {
            Typeface lato_regular = BNUtils.getLato_regular();
            final List<BNSite> sites = new ArrayList<>();

            for (String siteIdentifier : siteIdentifiers) {
                BNSite site = dataManager.getBNSite(siteIdentifier);
                if (site != null) {
                    sites.add(site);
                }
            }

            TextView tvSites = (TextView) findViewById(R.id.tvSitesNearYou);
            tvSites.setText(getResources().getString(R.string.OtherPlaces1) + currentSite.getTitle() + getResources().getString(R.string.OtherPlaces2));
            tvSites.setTypeface(lato_regular);
            tvSites.setLetterSpacing(0.3f);
            tvSites.setVisibility(View.VISIBLE);

            CardRecyclerView rvSites = (CardRecyclerView) findViewById(R.id.rvSitesNearYou);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            adapter = new BNSiteAdapter(this, sites, this, rvSites, false);
            adapter.setOnLoadMoreListener(new IBNLoadMoreSitesListener(){
                @Override
                public void onLoadMoreSites(boolean isFavourites) {
                    sites.add(null);
                    adapter.notifyItemInserted(sites.size() - 1);

                    sitesListener = new BNSitesListener(sites, isFavourites);
                    sitesListener.setIdentifier(currentSite.getOrganizationIdentifier());
                    sitesListener.setListener(SitesActivity.this);

                    String url = BNAppManager.getInstance().getNetworkManagerInstance().getMoreSitesUrl(biinie.getIdentifier());
                    Log.d(TAG, url);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            sitesListener,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    onLoadMoreError(error);
                                }
                            });
                    BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "MoreSitesData");
                }
            });
            rvSites.setLayoutManager(layoutManager);
            rvSites.setHasFixedSize(true);
            rvSites.setAdapter(adapter);
            rvSites.setVisibility(View.VISIBLE);
        }
    }

    private void loadMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.rlMapFragment);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void loadAnimations(){
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_in);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_out);
        inAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        outAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            LinearLayout layout = (LinearLayout)findViewById(R.id.vlMapSitesInclude);

            @Override
            public void onAnimationStart(Animation animation) {
                layout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            LinearLayout layout = (LinearLayout)findViewById(R.id.vlMapSitesInclude);

            @Override
            public void onAnimationStart(Animation animation) {
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        inAlpha.setAnimationListener(new Animation.AnimationListener() {
            LinearLayout layout = (LinearLayout)findViewById(R.id.vlMapSitesScreen);
            LinearLayout include = (LinearLayout)findViewById(R.id.vlMapSitesInclude);

            @Override
            public void onAnimationStart(Animation animation) {
                layout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.VISIBLE);
                layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        layout.startAnimation(outAlpha);
                        include.startAnimation(outAnimation);
                        return true;
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        outAlpha.setAnimationListener(new Animation.AnimationListener() {
            LinearLayout layout = (LinearLayout)findViewById(R.id.vlMapSitesScreen);

            @Override
            public void onAnimationStart(Animation animation) {
                layout.setVisibility(View.VISIBLE);
                layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setUpButtons(){
        TextView tvMapWaze, tvMapClose;
//        RideRequestButton tvMapUber;
        TextView tvMapUber;
        tvMapWaze = (TextView)findViewById(R.id.tvMapWaze);
        tvMapUber = (TextView)findViewById(R.id.tvMapUber);
//        tvMapUber = (RideRequestButton)findViewById(R.id.tvMapUber);
        tvMapClose = (TextView)findViewById(R.id.tvMapClose);

        tvMapWaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "waze://?ll=" + currentSite.getLatitude() + "," + currentSite.getLongitude() + "&navigate=yes";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                    startActivity(intent);
                }
            }
        });

//        tvMapUber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Uber", Toast.LENGTH_SHORT).show();
//            }
//        });
        Location pickUpLocation = BNAppManager.getInstance().getPositionManagerInstance().getLastLocation();
        double lat = (double)currentSite.getLatitude();
        double lon = (double)currentSite.getLongitude();
        RideParameters rideParams = new RideParameters.Builder()
                .setPickupLocation(pickUpLocation.getLatitude(), pickUpLocation.getLongitude(), null, null)
                .setDropoffLocation(lat, lon, currentSite.getTitle(), currentSite.getStreetAddress1())
                .build();
//        tvMapUber.setRideParameters(rideParams);

        tvMapClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)findViewById(R.id.vlMapSitesScreen);
                LinearLayout include = (LinearLayout)findViewById(R.id.vlMapSitesInclude);
                layout.startAnimation(outAlpha);
                include.startAnimation(outAnimation);
            }
        });
    }

    @Override
    public void onSiteLiked(String identifier, int position) {
        likeSite(identifier, true);
    }

    @Override
    public void onSiteUnliked(String identifier, int position) {
        likeSite(identifier, false);
    }

    private void likeSite(final String identifier, final boolean liked){
        BNSite site = dataManager.getBNSite(identifier);
        site.setUserLiked(liked);

        if(liked) {
            site.setLikeDate(Calendar.getInstance().getTime());
            dataManager.addFavouriteBNSite(site, 0);
            dataManager.removeNearByBNSite(identifier);
        }else{
            site.setLikeDate(null);
            dataManager.addNearByBNSite(site, 0);
            dataManager.removeFavouriteBNSite(identifier);
        }

        String url = BNAppManager.getInstance().getNetworkManagerInstance().getLikeUrl(biinie.getIdentifier(), liked);
        Log.d(TAG, url);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = site.getModel();
            request.put("model", model);
        }catch (JSONException e){
            Log.e(TAG, "Error:" + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                request,
                likesListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLikeError(error, identifier, liked);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "LikeSite");
    }

    @Override
    public void onLikeResponseOk() {

    }

    @Override
    public void onLikeResponseError() {

    }

    @Override
    public void onItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void onLoadMoreSitesResponse() {
        adapter.setLoaded();
    }

    private void onLoadMoreError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
        adapter.setLoaded();
    }

    private void onLikeError(VolleyError error, String identifier, boolean liked){
        Log.e(TAG, "Error:" + error.getMessage());
        if(liked) {
            dataManager.addPendingLikeSite(identifier);
        }else{
            dataManager.addPendingUnlikeSite(identifier);
        }
    }

    @Override
    public void onBack() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.EXIT_SITE_VIEW, currentSite.getIdentifier()));
    }

    @Override
    public void onLike() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.LIKE_SITE, currentSite.getIdentifier()));

        currentSite.setUserLiked(true);
        likeSite(currentSite.getIdentifier(), true);
    }

    @Override
    public void onUnlike() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.UNLIKE_SITE, currentSite.getIdentifier()));

        currentSite.setUserLiked(false);
        likeSite(currentSite.getIdentifier(), false);
    }

    @Override
    public void onShare() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.SHARE_SITE, currentSite.getIdentifier()));

        String text = BNAppManager.getInstance().getNetworkManagerInstance().getUrlBase() + "/sites/" + currentSite.getIdentifier();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, text);
        i.setType("text/plain");
        startActivity(Intent.createChooser(i, getString(R.string.ActionShare)));
    }

    @Override
    public void onLocation() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.vlMapSitesScreen);
        LinearLayout include = (LinearLayout)findViewById(R.id.vlMapSitesInclude);
        layout.startAnimation(inAlpha);
        include.startAnimation(inAnimation);
    }

    @Override
    public void onCall() {
        if(currentSite.getPhoneNumber() != null && !currentSite.getPhoneNumber().isEmpty()){
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + currentSite.getPhoneNumber()));
            startActivity(i);
        }else{
            Toast.makeText(this, R.string.NoPhone, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMail() {
        if(currentSite.getEmail() != null && !currentSite.getEmail().isEmpty()){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", currentSite.getEmail(), null));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.EmailMsj));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.EmailBody));

            startActivity(Intent.createChooser(intent, getResources().getString(R.string.EmailSend)));
        }else{
            Toast.makeText(this, R.string.NoMail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShowMore() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(currentSite.getLatitude(), currentSite.getLongitude());
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        analyticsManager.addAction(new BiinieAction(TAG, BiinieAction.EXIT_SITE_VIEW, currentSite.getIdentifier()));
    }
}
