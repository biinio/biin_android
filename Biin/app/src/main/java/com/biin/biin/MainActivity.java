package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Adapters.BNCategoryAdapter;
import com.biin.biin.Adapters.BNSiteAdapter;
import com.biin.biin.Components.CardRecyclerView;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Listeners.BNBiiniesListener;
import com.biin.biin.Entities.Listeners.BNInitialDataListener;
import com.biin.biin.Entities.Listeners.FlipperListener;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BNInitialDataListener.IBNInitialDataListener, BNBiiniesListener.IBNBiiniesListener {

    private BNBiiniesListener biiniesListener;
    private BNInitialDataListener initialDataListener;
    private ImageLoader imageLoader;
    private GestureDetector gestureDetector;

    private TextView tvRecomended, tvNearYou, tvFavouritePlaces;
    private LinearLayout hlRecomended;
    private ViewFlipper vfRecomended;

    private boolean fling = true;
    private long lastFling = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        BNUtils.setWidth(metrics.widthPixels);
        BNUtils.setDensity(metrics.density);

        initialize();

//        getBiinie();
//        getInitialData();
        loadData();
    }

    private void initialize(){
        Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
//        Typeface lato_light = Typeface.createFromAsset(getAssets(),"Lato-Light.ttf");

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
        vfRecomended = (ViewFlipper)findViewById(R.id.vfRecomended);

        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        gestureDetector = new GestureDetector(this, customGestureDetector);

        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    private void getBiinie(){
        biiniesListener = new BNBiiniesListener();
        biiniesListener.setListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getNetworkManagerInstance().getUrlGetBiiniesTest(),
                null,
                biiniesListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "Biinies");
    }

    private void getInitialData(){
        initialDataListener = new BNInitialDataListener();
        initialDataListener.setListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getNetworkManagerInstance().getUrlGetInitialDataTest(),
                null,
                initialDataListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "InitialData");
    }

    @Override
    public void onBiiniesLoaded() {
        /*Log.d("Biin", "Biinies loaded");
        Biinie biinie = BNAppManager.getDataManagerInstance().getBiinie();
        StringBuilder str = new StringBuilder();
        str.append("Biinie data:\n");
        str.append("FirstName: " + biinie.getFirstName() + "\n");
        str.append("LastName: " + biinie.getLastName() + "\n");
        str.append("Gender: " + biinie.getGender() + "\n");
        str.append("Email: " + biinie.getEmail());
        Log.d("Biin",  str.toString());*/
//        tvBiinies.setText(str.toString());
    }

    @Override
    public void onInitialDataLoaded() {
        /*Log.d("Biin", "Initial data loaded");
        HashMap<String,BNSite> sites = BNAppManager.getDataManagerInstance().getBNSites();
        HashMap<String,BNOrganization> organizations = BNAppManager.getDataManagerInstance().getBNOrganizations();
        HashMap<String,BNElement> elements = BNAppManager.getDataManagerInstance().getBNElements();
        List<BNHighlight> highlights = BNAppManager.getDataManagerInstance().getBNHighlights();
        HashMap<String,BNCategory> categories = BNAppManager.getDataManagerInstance().getBNCategories();
        StringBuilder str = new StringBuilder();
        str.append("Initial data:\n");
        str.append("Sites: " + sites.size() + "\n");
        str.append("Organizations: " + organizations.size() + "\n");
        str.append("Elements: " + elements.size() + "\n");
        str.append("Highlights: " + highlights.size() + "\n");
        str.append("Categories: " + categories.size());
        Log.d("Biin",  str.toString());*/
    }

    public void loadData() {
        loadRecomendations();
        loadNearPlaces();
        loadFavourites();
        loadCategories();
    }

    private void setPaggingDots(int total){
        float density = BNUtils.getDensity();
        ImageView dotBig = new ImageView(this);
        dotBig.setImageResource(R.drawable.pagging_dot);
        dotBig.setColorFilter(getResources().getColor(R.color.colorAccent));
        dotBig.setLayoutParams(new LinearLayout.LayoutParams((int)(20 * density), (int)(9 * density)));
        hlRecomended.addView(dotBig);
        for (int i = 1; i < total; i++) {
            ImageView dotSmall = new ImageView(this);
            dotSmall.setImageResource(R.drawable.pagging_dot);
            dotSmall.setColorFilter(getResources().getColor(R.color.colorPrimary));
            dotSmall.setLayoutParams(new LinearLayout.LayoutParams((int)(20 * density), (int)(7 * density)));
            hlRecomended.addView(dotSmall);
        }
    }

    private void loadRecomendations(){
        HashMap<String,BNElement> elements = BNAppManager.getDataManagerInstance().getBNElements();
        List<BNHighlight> highlights = BNAppManager.getDataManagerInstance().getBNHighlights();
        setPaggingDots(highlights.size());

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        for (BNHighlight highlight : highlights) {
            final BNElement element = elements.get(highlight.getIdentifier());
            element.setShowcase(BNAppManager.getDataManagerInstance().getBNShowcase(highlight.getShowcaseIdentifier()));
            element.getShowcase().setSite(BNAppManager.getDataManagerInstance().getBNSite(highlight.getSiteIdentifier()));

            View view = inflater.inflate(R.layout.bnelement_item, null);

            RelativeLayout rlElementLabel;
            FrameLayout flOffer;
            TextView tvTitle, tvSubtitle, tvSubtitleLocation, tvPrice, tvDiscount, tvOffer;
            final ProgressBar pbElement, pbOrganization;
            final ImageView ivElement, ivOrganization, ivOffer;

            rlElementLabel = (RelativeLayout)view.findViewById(R.id.rlElementLabel);
            flOffer = (FrameLayout)view.findViewById(R.id.flElementOffer);

            ivElement = (ImageView)view.findViewById(R.id.ivSite);
            ivOrganization = (ImageView)view.findViewById(R.id.ivOrganization);
            ivOffer = (ImageView)view.findViewById(R.id.ivElementOffer);

            tvTitle = (TextView)view.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView)view.findViewById(R.id.tvSubtitle);
            tvSubtitleLocation = (TextView)view.findViewById(R.id.tvSubtitleLocation);
            tvPrice = (TextView)view.findViewById(R.id.tvPrice);
            tvDiscount = (TextView)view.findViewById(R.id.tvDiscount);
            tvOffer = (TextView)view.findViewById(R.id.tvElementOffer);

            pbElement = (ProgressBar)view.findViewById(R.id.pbElement);
            pbOrganization = (ProgressBar)view.findViewById(R.id.pbOrganization);

            Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
            Typeface lato_black = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");

            tvTitle.setTypeface(lato_regular);
            tvSubtitle.setTypeface(lato_black);
            tvSubtitleLocation.setTypeface(lato_regular);
            tvPrice.setTypeface(lato_regular);
            tvDiscount.setTypeface(lato_regular);
            tvOffer.setTypeface(lato_black);

            imageLoader.get(element.getMedia().get(0).getUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    ivElement.setImageBitmap(response.getBitmap());
                    pbElement.setVisibility(View.GONE);
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            imageLoader.get(element.getShowcase().getSite().getMedia().get(0).getUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    ivOrganization.setImageBitmap(response.getBitmap());
                    pbOrganization.setVisibility(View.GONE);
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            rlElementLabel.setBackgroundColor(element.getShowcase().getSite().getOrganization().getPrimaryColor());
            tvTitle.setText(element.getTitle());
            tvTitle.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
            tvSubtitle.setText(element.getShowcase().getSite().getTitle());
            tvSubtitle.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
            tvSubtitleLocation.setText(element.getShowcase().getSite().getSubTitle());
            tvSubtitleLocation.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
//            tvPrice.setText(element.getListPrice());
            tvPrice.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
            tvDiscount.setText("Ȼ" + element.getPrice());
            tvDiscount.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());

            if(element.isHasDiscount() && !element.getDiscount().isEmpty()) {
                tvOffer.setText("-" + element.getDiscount() + "%");
                tvOffer.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
                ivOffer.setColorFilter(element.getShowcase().getSite().getOrganization().getPrimaryColor());
                flOffer.setVisibility(View.VISIBLE);
            }else{
                flOffer.setVisibility(View.GONE);
            }

            FrameLayout flElement = new FrameLayout(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, ElementsActivity.class);
                    SharedPreferences preferences = MainActivity.this.getSharedPreferences(MainActivity.this.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(BNUtils.BNStringExtras.BNElement, element.getIdentifier());
                    editor.commit();
                    i.putExtra(BNUtils.BNStringExtras.BNShowMore, true);
                    MainActivity.this.startActivity(i);
                }
            });

            flElement.addView(view);
            vfRecomended.addView(flElement);
        }
        vfRecomended.setInAnimation(MainActivity.this, R.anim.left_in);
        vfRecomended.setOutAnimation(MainActivity.this, R.anim.left_out);

//        findViewById(R.id.nvMain).setOnTouchListener(new FlipperListener(gestureDetector));
        findViewById(R.id.vfRecomended).setOnTouchListener(new FlipperListener(gestureDetector));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fling) {
                    showRecomended(true, true);
                }
                handler.postDelayed(this, 4000);
            }
        }, 8000);
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                vfRecomended.setInAnimation(MainActivity.this, R.anim.left_in);
                vfRecomended.setOutAnimation(MainActivity.this, R.anim.left_out);
                showRecomended(true, false);
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                vfRecomended.setInAnimation(MainActivity.this, R.anim.right_in);
                vfRecomended.setOutAnimation(MainActivity.this, R.anim.right_out);
                showRecomended(false, false);

                vfRecomended.setInAnimation(MainActivity.this, R.anim.left_in);
                vfRecomended.setOutAnimation(MainActivity.this, R.anim.left_out);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void showRecomended(boolean forward, boolean play){
        fling = play;
        if(forward){
            vfRecomended.showNext();
            View view = hlRecomended.getChildAt(hlRecomended.getChildCount() - 1);
            hlRecomended.removeView(view);
            hlRecomended.addView(view, 0);
        }else{
            vfRecomended.showPrevious();
            View view = hlRecomended.getChildAt(0);
            hlRecomended.removeView(view);
            hlRecomended.addView(view, hlRecomended.getChildCount());
        }
        if(!play){
            lastFling = System.currentTimeMillis();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (lastFling + 4500 > System.currentTimeMillis()) {
                        fling = true;
                    }
                }
            }, 8000);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
//        return true;
        return super.onTouchEvent(event);
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

        CardRecyclerView rvSites = (CardRecyclerView)findViewById(R.id.rvFavouritePlaces);
//        rvSites.setSnapEnabled(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        BNSiteAdapter adapter = new BNSiteAdapter(this, sites);
        adapter.setShowOthers(true);
        rvSites.setLayoutManager(layoutManager);
        rvSites.setHasFixedSize(true);
        rvSites.setAdapter(adapter);

        findViewById(R.id.ivFavouritePlaces).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SitesListActivity.class);
                i.putExtra(BNUtils.BNStringExtras.BNFavorites, true);
                MainActivity.this.startActivity(i);
            }
        });
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
                        SharedPreferences preferences = MainActivity.this.getSharedPreferences(MainActivity.this.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(BNUtils.BNStringExtras.BNCategory, category.getIdentifier());
                        editor.commit();
//                        i.putExtra(BNUtils.BNStringExtras.BNShowMore, true);
                        MainActivity.this.startActivity(i);
                    }
                });

                // attach del view a la pantalla principal
                layout.addView(view);
            }
        }
    }
}































