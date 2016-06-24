package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biin.biin.Adapters.BNCategoryAdapter;
import com.biin.biin.Adapters.BNHighlightAdapter;
import com.biin.biin.Adapters.BNSiteAdapter;
import com.biin.biin.Components.CardRecyclerView;
import com.biin.biin.Components.LinearLayoutManagerSmooth;
import com.biin.biin.Components.Listeners.BNAdapterListener;
import com.biin.biin.Components.Listeners.HighlightsPagerListener;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Utils.BNUtils.BNStringTypes;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity implements HighlightsPagerListener.IBNHighlightsListener, BNAdapterListener, BNAdapterListener.BNSitesLikeListener {

    private static final String TAG = "MainActivity";

    private TextView tvRecomended, tvNearYou, tvFavouritePlaces;
    private TextView tvProfile, tvFavourites, tvFriends, tvAbout;
    private TextView tvCloseApp, tvConfirmClose, tvDontClose;
    private CardRecyclerView rvNearSites, rvFavouritePlaces;
    private LinearLayout hlRecomended;
    private RelativeLayout rlCloseApp;
    private DrawerLayout drawer;

    private BNDataManager dataManager;
    private List<BNSite> nearSites;
    private List<BNSite> favoriteSites;
    private BNSiteAdapter nearPlacesAdapter;
    private BNSiteAdapter favoritesAdapter;

//    private long nearAnimDuration;
//    private long favsAnimDuration;
    private long animDuration = 300;

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
        rlCloseApp = (RelativeLayout)findViewById(R.id.rlCloseApp);
        rlCloseApp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        tvCloseApp = (TextView)findViewById(R.id.tvCloseApp);
        tvCloseApp.setTypeface(lato_regular);
        tvCloseApp.setLetterSpacing(0.3f);

        tvConfirmClose = (TextView)findViewById(R.id.tvConfirmClose);
        tvConfirmClose.setTypeface(lato_regular);
        tvConfirmClose.setLetterSpacing(0.3f);
        tvConfirmClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });

        tvDontClose = (TextView)findViewById(R.id.tvDontClose);
        tvDontClose.setTypeface(lato_regular);
        tvDontClose.setLetterSpacing(0.3f);
        tvDontClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlCloseApp.setVisibility(View.GONE);
            }
        });

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
        dataManager = BNAppManager.getDataManagerInstance();

        loadRecomendations();
        loadFavourites();
        loadNearPlaces();
        loadCategories();
    }

    private void loadRecomendations(){
        LinkedHashMap<String,BNElement> allElements = dataManager.getBNElements();
        List<BNHighlight> highlights = dataManager.getBNHighlights();
        List<BNElement> elements = new ArrayList<>();

        for (BNHighlight highlight : highlights) {
            BNElement element = allElements.get(highlight.getIdentifier());
            BNShowcase showcase = dataManager.getBNShowcase(highlight.getShowcaseIdentifier());
            BNSite site = dataManager.getBNSite(highlight.getSiteIdentifier());

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
        if(rlCloseApp.getVisibility() == View.GONE) {
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
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
        nearSites = new ArrayList<>(dataManager.getNearByBNSites(false).values());
        List<String> organizations = new ArrayList<>();
        List<BNSite> removeSites = new ArrayList<>();

        for (BNSite site : nearSites) {
            if(organizations.contains(site.getOrganizationIdentifier())) {
                removeSites.add(site);
            }else{
                organizations.add(site.getOrganizationIdentifier());
            }
        }

        for (BNSite site : removeSites) {
            nearSites.remove(site);
        }

        if(nearSites.size() > 0) {
            showNearPlacesList();
        }else{
            showNearPlacesEmpty(true);
        }
    }

    private void showNearPlacesList(){
        rvNearSites = (CardRecyclerView) findViewById(R.id.rvOtherNearYou);

        /*SlideInUpAnimator animator = new SlideInUpAnimator();
//        nearAnimDuration = animator.getRemoveDuration();
        animator.setAddDuration(animDuration);
        animator.setRemoveDuration(animDuration);
        rvNearSites.setItemAnimator(animator);*/

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.HORIZONTAL, false);

        nearPlacesAdapter = new BNSiteAdapter(this, nearSites, this, animDuration);
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

    private void showNearPlacesEmpty(boolean empty){
        CardRecyclerView rvNearYou = (CardRecyclerView) findViewById(R.id.rvOtherNearYou);
        ImageView ivNearYou = (ImageView) findViewById(R.id.ivOtherNearYou);
        LinearLayout vlNearYou = (LinearLayout) findViewById(R.id.vlEmptyNearPlaces);

        if(empty) {
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

    private void loadFavourites(){
        favoriteSites = new ArrayList<>(dataManager.getFavouriteBNSites().values());

        if(favoriteSites.size() > 0) {
            showFavouritesList();
        }else{
            showFavouritesEmpty(true);
        }
    }

    private void showFavouritesList(){
        rvFavouritePlaces = (CardRecyclerView) findViewById(R.id.rvFavouritePlaces);
        ImageView ivFavouritePlaces = (ImageView) findViewById(R.id.ivFavouritePlaces);

        /*SlideInDownAnimator animator = new SlideInDownAnimator();
//        favsAnimDuration = animator.getRemoveDuration();
        animator.setAddDuration(animDuration);
        animator.setRemoveDuration(animDuration);
        rvFavouritePlaces.setItemAnimator(animator);*/

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.HORIZONTAL, false);

        favoritesAdapter = new BNSiteAdapter(this, favoriteSites, this, animDuration);
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

    private void showFavouritesEmpty(boolean empty){
        CardRecyclerView rvFavouritePlaces = (CardRecyclerView) findViewById(R.id.rvFavouritePlaces);
        ImageView ivFavouritePlaces = (ImageView) findViewById(R.id.ivFavouritePlaces);
        LinearLayout vlFavourites = (LinearLayout) findViewById(R.id.vlEmptyFavorites);

        if(empty) {
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

    private void loadCategories(){
        Typeface lato_regular = BNUtils.getLato_regular();
        // layout al que se va a agregar las listas de categorias
        LinearLayout layout = (LinearLayout)findViewById(R.id.vlShowcases);

        // obtener la lista de categorias
        List<BNCategory> categories = new ArrayList<>(dataManager.getBNCategories().values());
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

    @Override
    public void onBackPressed() {
        if(rlCloseApp.getVisibility() == View.GONE){
            rlCloseApp.setVisibility(View.VISIBLE);
        }else{
            rlCloseApp.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmptyAdapter(String type) {
        if(type.equals(BNStringTypes.NearSites)) {
            showNearPlacesEmpty(true);
        }else if(type.equals(BNStringTypes.FavouriteSites)) {
            showFavouritesEmpty(true);
        }
    }

    @Override
    public void onLoadAdapter(String type) {
        if(type.equals(BNStringTypes.NearSites)) {
            showNearPlacesEmpty(false);
        }else if(type.equals(BNStringTypes.FavouriteSites)) {
            showFavouritesEmpty(false);
        }
    }

    @Override
    public void onSiteLiked(String identifier) {
        boolean added = dataManager.likeBNSite(identifier);

        addFavouriteSite(identifier);
        favoritesAdapter.notifyItemInserted(0);
        rvFavouritePlaces.smoothScrollToPosition(0);

        final int position = removeNearSite(identifier);
        if(position > -1){
            nearPlacesAdapter.notifyItemRemoved(position);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                favoritesAdapter.notifyItemRangeChanged(0, favoriteSites.size());
                favoritesAdapter.notifyDataSetChanged();
                if(position > -1) {
                    nearPlacesAdapter.notifyItemRangeChanged(position, nearSites.size());
                    nearPlacesAdapter.notifyDataSetChanged();
                }
            }
        }, animDuration + 300);
    }

    @Override
    public void onSiteUnliked(String identifier) {
        boolean removed = dataManager.unlikeBNSite(identifier);

        addNearSite(identifier);
        nearPlacesAdapter.notifyItemInserted(0);
        rvNearSites.smoothScrollToPosition(0);

        final int position = removeFavouriteSite(identifier);
        if(position > -1){
            favoritesAdapter.notifyItemRemoved(position);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nearPlacesAdapter.notifyItemRangeChanged(0, nearSites.size());
                nearPlacesAdapter.notifyDataSetChanged();
                if(position > -1) {
                    favoritesAdapter.notifyItemRangeChanged(position, favoriteSites.size());
                    favoritesAdapter.notifyDataSetChanged();
                }
            }
        }, animDuration + 300);
    }

    private boolean addNearSite(String identifier){
        boolean attach = true;
        int size = nearSites.size();

        if(size > 0) {
            int i = 0;
            do {
                if (nearSites.get(i).getIdentifier().equals(identifier)) {
                    attach = false;
                }
                i++;
            } while (i < size && attach);
        }

        if(attach){
            if(size == 0){
                showNearPlacesEmpty(false);
            }
            nearSites.add(0, dataManager.getBNSite(identifier));
        }

        return attach;
    }

    private int removeNearSite(String identifier){
        int index = -1;
        int size = nearSites.size();

        if(size > 0) {
            int i = 0;
            do {
                if (nearSites.get(i).getIdentifier().equals(identifier)) {
                    nearSites.remove(i);
                    index = i;
                }
                i++;
            } while (i < size && index == -1);
        }

        if(size == 1 && index == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showNearPlacesEmpty(true);

                }
            }, animDuration + 300);
        }

        return index;
    }

    private boolean addFavouriteSite(String identifier){
        boolean attach = true;
        int size = favoriteSites.size();

        if(size > 0) {
            int i = 0;
            do {
                if (favoriteSites.get(i).getIdentifier().equals(identifier)) {
                    attach = false;
                }
                i++;
            } while (i < size && attach);
        }

        if(attach){
            if(size == 0){
                showFavouritesEmpty(false);
            }
            favoriteSites.add(0, dataManager.getBNSite(identifier));
        }

        return attach;
    }

    private int removeFavouriteSite(String identifier){
        int index = -1;
        int size = favoriteSites.size();

        if(size > 0) {
            int i = 0;
            do {
                if (favoriteSites.get(i).getIdentifier().equals(identifier)) {
                    favoriteSites.remove(i);
                    index = i;
                }
                i++;
            } while (i < size && index == -1);

            if(size == 1 && index == 0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showFavouritesEmpty(true);
                    }
                }, animDuration + 300);
            }
        }

        return index;
    }

}































