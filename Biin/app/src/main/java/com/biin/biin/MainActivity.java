package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.ViewFlipper;

import com.biin.biin.Adapters.BNCategoryAdapter;
import com.biin.biin.Adapters.BNHighlightAdapter;
import com.biin.biin.Adapters.BNSiteAdapter;
import com.biin.biin.Components.CardRecyclerView;
import com.biin.biin.Components.Listeners.HighlightsPagerListener;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HighlightsPagerListener.IBNHighlightsListener {

    private static final String TAG = "MainActivity";

    private TextView tvRecomended, tvNearYou, tvFavouritePlaces;
    private TextView tvProfile, tvFavourites, tvFriends, tvAbout;
    private LinearLayout hlRecomended;
    private DrawerLayout drawer;

    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        BNUtils.setWidth(metrics.widthPixels);
        BNUtils.setDensity(metrics.density);

        setUpScreen();

        loadData();
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();

        tvRecomended = (TextView)findViewById(R.id.tvRecomended);
        tvRecomended.setTypeface(lato_regular);
        tvRecomended.setLetterSpacing(0.3f);

        tvNearYou = (TextView)findViewById(R.id.tvOtherNearYou);
        tvNearYou.setTypeface(lato_regular);
        tvNearYou.setLetterSpacing(0.3f);

        tvFavouritePlaces = (TextView)findViewById(R.id.tvFavouritePlaces);
        tvFavouritePlaces.setTypeface(lato_regular);
        tvFavouritePlaces.setLetterSpacing(0.3f);

        hlRecomended = (LinearLayout)findViewById(R.id.hlRecomended);

        setUpDrawer(lato_regular);
    }

    private void setUpDrawer(Typeface lato_regular){
        drawer = (DrawerLayout) findViewById(R.id.dlMain);

        tvProfile = (TextView)findViewById(R.id.tvMenuProfile);
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

        tvFavourites = (TextView)findViewById(R.id.tvMenuFavourites);
        tvFavourites.setTypeface(lato_regular);
        tvFavourites.setLetterSpacing(0.3f);
        tvFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ElementsListActivity.class);
                SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BNUtils.BNStringExtras.BNCategory, "favorites");
                editor.commit();

                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        tvFriends = (TextView)findViewById(R.id.tvMenuFriends);
        tvFriends.setTypeface(lato_regular);
        tvFriends.setLetterSpacing(0.3f);

        tvAbout = (TextView)findViewById(R.id.tvMenuAbout);
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

        ImageView ivMenu = (ImageView)findViewById(R.id.ivToolbarMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        RelativeLayout rlDrawer = (RelativeLayout)findViewById(R.id.bnNavView);
        rlDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void loadData() {
        loadRecomendations();
        loadNearPlaces();
        loadFavourites();
        loadCategories();
    }

    private void loadRecomendations(){
        HashMap<String,BNElement> allElements = BNAppManager.getDataManagerInstance().getBNElements();
        List<BNHighlight> highlights = BNAppManager.getDataManagerInstance().getBNHighlights();
        List<BNElement> elements = new ArrayList<>();

        for (BNHighlight highlight : highlights) {
            BNElement element = allElements.get(highlight.getIdentifier());
            BNShowcase showcase = BNAppManager.getDataManagerInstance().getBNShowcase(highlight.getShowcaseIdentifier());
            BNSite site = BNAppManager.getDataManagerInstance().getBNSite(highlight.getSiteIdentifier());

            if(element != null && showcase != null && site != null) {
                showcase.setSite(site);
                element.setShowcase(showcase);
                elements.add(element);
            }else{
                Log.e(TAG, "No se encontraron los datos completos del elemento (element, showcase y site)");
            }
        }
        total = elements.size();

        if(total > 0) {
            HighlightsPagerListener listener = new HighlightsPagerListener(this);
            listener.setLenght(total);
//            setPaggingDots(total);

            RollPagerView pvHighlights = (RollPagerView) findViewById(R.id.pvRecomended);
            pvHighlights.setAnimationDurtion(1000);
            pvHighlights.setHintView(null);
            pvHighlights.getViewPager().addOnPageChangeListener(listener);
            BNHighlightAdapter adapter = new BNHighlightAdapter(pvHighlights, this);
            adapter.setHighlights(elements);
            pvHighlights.setAdapter(adapter);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(BNUtils.getWidth(), BNUtils.getWidth() + (int)(68 * BNUtils.getDensity()));
            params.addRule(RelativeLayout.BELOW, R.id.hlRecomended);
            pvHighlights.setLayoutParams(params);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setPaggingDots(position);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);
        }
        return false;
    }

    private void setPaggingDots(int position){
        float density = BNUtils.getDensity();
        hlRecomended.removeAllViews();
        for (int i = 0; i < total; i++) {
            if(i == position){
                ImageView dotBig = new ImageView(this);
                dotBig.setImageResource(R.drawable.pagging_dot);
                dotBig.setColorFilter(getResources().getColor(R.color.colorAccentGray));
                dotBig.setLayoutParams(new LinearLayout.LayoutParams((int)(20 * density), (int)(9 * density)));
                hlRecomended.addView(dotBig);
            }else {
                ImageView dotSmall = new ImageView(this);
                dotSmall.setImageResource(R.drawable.pagging_dot);
                dotSmall.setColorFilter(getResources().getColor(R.color.colorPrimary));
                dotSmall.setLayoutParams(new LinearLayout.LayoutParams((int) (20 * density), (int) (7 * density)));
                hlRecomended.addView(dotSmall);
            }
        }
    }

    private void loadNearPlaces(){
        List<BNSite> sites = new ArrayList<>(BNAppManager.getDataManagerInstance().getNearByBNSites().values());
        List<BNSite> favs = new ArrayList<>(BNAppManager.getDataManagerInstance().getFavouriteBNSites().values());

        for (BNSite site : favs) {
            if(sites.contains(site)){
                sites.remove(site);
            }
        }

        CardRecyclerView rvSites = (CardRecyclerView)findViewById(R.id.rvOtherNearYou);
//        rvSites.setSnapEnabled(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        BNSiteAdapter adapter = new BNSiteAdapter(this, sites);
        adapter.setShowOthers(true);
        rvSites.setLayoutManager(layoutManager);
        rvSites.setHasFixedSize(true);
        rvSites.setAdapter(adapter);

        findViewById(R.id.ivOtherNearYou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SitesListActivity.class);
                i.putExtra(BNUtils.BNStringExtras.BNFavorites, false);
                MainActivity.this.startActivity(i);
            }
        });
    }

    private void loadFavourites(){
        List<BNSite> sites = new ArrayList<>(BNAppManager.getDataManagerInstance().getFavouriteBNSites().values());
        CardRecyclerView rvFavouritePlaces = (CardRecyclerView) findViewById(R.id.rvFavouritePlaces);
        ImageView ivFavouritePlaces = (ImageView) findViewById(R.id.ivFavouritePlaces);

        if(sites.size() > 0) {
//        rvFavouritePlaces.setSnapEnabled(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            BNSiteAdapter adapter = new BNSiteAdapter(this, sites);
            adapter.setShowOthers(true);
            rvFavouritePlaces.setLayoutManager(layoutManager);
            rvFavouritePlaces.setHasFixedSize(true);
            rvFavouritePlaces.setAdapter(adapter);

            ivFavouritePlaces.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, SitesListActivity.class);
                    i.putExtra(BNUtils.BNStringExtras.BNFavorites, true);
                    MainActivity.this.startActivity(i);
                }
            });
        }else{
            tvFavouritePlaces.setVisibility(View.GONE);
            ivFavouritePlaces.setVisibility(View.GONE);
            rvFavouritePlaces.setVisibility(View.GONE);

            RelativeLayout layout = (RelativeLayout)findViewById(R.id.rvFavourites);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            layout.getLayoutParams().height = (BNUtils.getWidth() / 2) + (int)(48 * BNUtils.getDensity());

            View view = inflater.inflate(R.layout.add_favourites, null);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (BNUtils.getWidth() / 2) + (int)(48 * BNUtils.getDensity()));
            view.setLayoutParams(params);

            Typeface lato_regular = BNUtils.getLato_regular();
            TextView tvFavourites = (TextView) view.findViewById(R.id.tvEmptyFavourites);
            tvFavourites.setTypeface(lato_regular);
            tvFavourites.setLetterSpacing(0.3f);

            layout.addView(view);
        }
    }

    private void loadCategories(){
        Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
        // layout al que se va a agregar las listas de categorias
        LinearLayout layout = (LinearLayout)findViewById(R.id.vlShowcases);

        // obtener la lista de categorias
        List<BNCategory> categories = new ArrayList<>(BNAppManager.getDataManagerInstance().getBNCategories().values());
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        for (final BNCategory category : categories) {
            // obtener la lista de elementos de cada categoria
            List<BNElement> categoryElements = category.getElements();
            if(categoryElements.size() > 0) {
                // poner el label (titulo) y setear el typeface
                View view = inflater.inflate(R.layout.bncategory_list, null);
                TextView tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
                ImageView ivCategoryName = (ImageView) view.findViewById(R.id.ivCategoryName);

                tvCategoryName.setTypeface(lato_regular);
                tvCategoryName.setLetterSpacing(0.3f);
                tvCategoryName.setText(getResources().getIdentifier(category.getIdentifier(),"string",getPackageName()));

                // llenar el recyclerview
                CardRecyclerView rvCategoryList = (CardRecyclerView)view.findViewById(R.id.rvCategoryList);
//                rvCategoryList.setSnapEnabled(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                BNCategoryAdapter adapter = new BNCategoryAdapter(this, categoryElements);
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
}































