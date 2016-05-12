package com.biin.biin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.CardView.BNCategoryAdapter;
import com.biin.biin.CardView.BNElementAdapter;
import com.biin.biin.CardView.BNSiteAdapter;
import com.biin.biin.CardView.CardRecyclerView;
import com.biin.biin.CardView.SnappingRecyclerView;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Entities.Listeners.BNBiiniesListener;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Entities.Listeners.BNInitialDataListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BNInitialDataListener.IBNInitialDataListener, BNBiiniesListener.IBNBiiniesListener {

    private BNBiiniesListener biiniesListener;
    private BNInitialDataListener initialDataListener;

    private TextView tvRecomended, tvNearYou;
    private RecyclerView rvRecomended, rvNearYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        BNUtils.setWidth(metrics.widthPixels);
        BNUtils.setDensity(metrics.density);

        setTextViewsStyles();

        getBiinie();
        getInitialData();
    }

    private void setTextViewsStyles(){
        Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
        Typeface lato_light = Typeface.createFromAsset(getAssets(),"Lato-Light.ttf");

        tvRecomended = (TextView)findViewById(R.id.tvRecomended);
        tvRecomended.setTypeface(lato_regular);
        tvRecomended.setLetterSpacing(0.3f);

        tvNearYou = (TextView)findViewById(R.id.tvNearYou);
        tvNearYou.setTypeface(lato_regular);
        tvNearYou.setLetterSpacing(0.3f);

        rvRecomended = (RecyclerView)findViewById(R.id.rvRecomended);
        rvNearYou = (RecyclerView)findViewById(R.id.rvNearYou);

//        rvRecomended.setLayoutParams(new RelativeLayout.LayoutParams(BNUtils.getWidth(), BNUtils.getWidth() + (68 * (int)BNUtils.getDensity())));
//        rvNearYou.setLayoutParams(new RelativeLayout.LayoutParams(BNUtils.getWidth() / 2, (BNUtils.getWidth() + (56 * (int)BNUtils.getDensity())) / 2));
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
        Log.d("Biin", "Biinies loaded");
        Biinie biinie = BNAppManager.getDataManagerInstance().getBiinie();
        StringBuilder str = new StringBuilder();
        str.append("Biinie data:\n");
        str.append("FirstName: " + biinie.getFirstName() + "\n");
        str.append("LastName: " + biinie.getLastName() + "\n");
        str.append("Gender: " + biinie.getGender() + "\n");
        str.append("Email: " + biinie.getEmail());
        Log.d("Biin",  str.toString());
//        tvBiinies.setText(str.toString());
    }

    @Override
    public void onInitialDataLoaded() {
        Log.d("Biin", "Initial data loaded");
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
        Log.d("Biin",  str.toString());

        loadRecomendations();
        loadNearPlaces();
        loadCategories();
    }

    private void loadRecomendations(){
        HashMap<String,BNElement> elements = BNAppManager.getDataManagerInstance().getBNElements();
        List<BNHighlight> highlights = BNAppManager.getDataManagerInstance().getBNHighlights();

        SnappingRecyclerView rvHighlights = (SnappingRecyclerView)findViewById(R.id.rvRecomended);
        rvHighlights.setSnapEnabled(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        List<BNElement> highlightElements = new ArrayList<>();
        for (BNHighlight highlight : highlights) {
            BNElement element = elements.get(highlight.getIdentifier());
            element.set_id(highlight.get_id());
            highlightElements.add(element);
        }

        BNElementAdapter adapter = new BNElementAdapter(this, highlightElements);
        rvHighlights.setLayoutManager(layoutManager);
        rvHighlights.setHasFixedSize(true);
        rvHighlights.setAdapter(adapter);
    }

    private void loadNearPlaces(){
        List<BNSite> sites = new ArrayList<>(BNAppManager.getDataManagerInstance().getBNSites().values());

        CardRecyclerView rvSites = (CardRecyclerView)findViewById(R.id.rvNearYou);
//        rvSites.setSnapEnabled(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        BNSiteAdapter adapter = new BNSiteAdapter(this, sites);
        rvSites.setLayoutManager(layoutManager);
        rvSites.setHasFixedSize(true);
        rvSites.setAdapter(adapter);
    }

    private void loadCategories(){
        Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
        // layout al que se va a agregar las listas de categorias
        LinearLayout layout = (LinearLayout)findViewById(R.id.vlCategories);

        // obtener la lista de categorias
        List<BNCategory> categories = new ArrayList<>(BNAppManager.getDataManagerInstance().getBNCategories().values());

        for (BNCategory category : categories) {
            // obtener la lista de elementos de cada categoria
            List<BNElement> categoryElements = category.getElements();
            if(categoryElements.size() > 0) {
                // poner el label (titulo) y setear el typeface
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.bncategory_list, null);
                TextView tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
                tvCategoryName.setTypeface(lato_regular);
                tvCategoryName.setLetterSpacing(0.3f);
                tvCategoryName.setText(category.getIdentifier());

                if (categoryElements.size() > 1) {
                    //TODO llenar el recyclerview
                    CardRecyclerView rvCategoryList = (CardRecyclerView)view.findViewById(R.id.rvCategoryList);
//                    rvCategoryList.setSnapEnabled(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                    BNCategoryAdapter adapter = new BNCategoryAdapter(this, categoryElements);
                    rvCategoryList.setLayoutManager(layoutManager);
                    rvCategoryList.setHasFixedSize(true);
                    rvCategoryList.setAdapter(adapter);
                } else {
                    //TODO poner el unico element
                }

                //TODO attach del view en la pantalla principal
                layout.addView(view);
            }
        }
    }

}































