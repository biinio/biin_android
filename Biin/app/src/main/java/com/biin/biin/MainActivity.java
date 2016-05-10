package com.biin.biin;

import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.CardView.BNElementAdapter;
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

    private TextView tvBiinies,tvInitialData,tvTitle,tvSubtitle,tvPrice,tvDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tvBiinies = (TextView)findViewById(R.id.tvBiinie);
//        tvInitialData = (TextView)findViewById(R.id.tvInitialData);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        BNUtils.setWidth(metrics.widthPixels);

        /* probando la interface de element y la tipografia * /
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvSubtitle = (TextView)findViewById(R.id.tvSubtitle);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvDiscount = (TextView)findViewById(R.id.tvDiscount);

        Typeface lato_rg = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
        Typeface lato_li = Typeface.createFromAsset(getAssets(),"Lato-Light.ttf");

        tvTitle.setTypeface(lato_rg);
        tvSubtitle.setTypeface(lato_li);
        tvPrice.setTypeface(lato_li);
        tvDiscount.setTypeface(lato_rg);
        /*****************************************************/


        getBiinie();
        getInitialData();
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
//        HashMap<String,BNSite> sites = BNAppManager.getDataManagerInstance().getBNSites();
//        HashMap<String,BNOrganization> organizations = BNAppManager.getDataManagerInstance().getBNOrganizations();
        HashMap<String,BNElement> elements = BNAppManager.getDataManagerInstance().getBNElements();
        List<BNHighlight> highlights = BNAppManager.getDataManagerInstance().getBNHighlights();
//        HashMap<String,BNCategory> categories = BNAppManager.getDataManagerInstance().getBNCategories();
//        StringBuilder str = new StringBuilder();
//        str.append("Initial data:\n");
//        str.append("Sites: " + sites.size() + "\n");
//        str.append("Organizations: " + organizations.size() + "\n");
//        str.append("Elements: " + elements.size() + "\n");
//        str.append("Highlights: " + highlights.size() + "\n");
//        str.append("Categories: " + categories.size());
//        Log.d("Biin",  str.toString());
//        tvInitialData.setText(str.toString());

        SnappingRecyclerView rvHighlights = (SnappingRecyclerView)findViewById(R.id.rvHighlights);
        rvHighlights.setSnapEnabled(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<BNElement> highlightElements = new ArrayList<>();

        for (BNHighlight hl : highlights) {
            highlightElements.add(elements.get(hl.getIdentifier()));
        }

        BNElementAdapter adapter = new BNElementAdapter(this, highlightElements);
        rvHighlights.setLayoutManager(layoutManager);
        rvHighlights.setHasFixedSize(true);
        rvHighlights.setAdapter(adapter);
    }

}
