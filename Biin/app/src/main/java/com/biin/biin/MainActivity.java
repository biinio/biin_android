package com.biin.biin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Entities.Listeners.BNBiiniesListener;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Entities.Listeners.BNInitialDataListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BNInitialDataListener.IBNInitialDataListener, BNBiiniesListener.IBNBiiniesListener {

    private BNBiiniesListener biiniesListener;
    private BNInitialDataListener initialDataListener;

    private TextView tvBiinies;
    private TextView tvInitialData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBiinies = (TextView)findViewById(R.id.textViewBiinie);
        tvInitialData = (TextView)findViewById(R.id.textViewInitialData);

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
        tvBiinies.setText(str.toString());
    }

    @Override
    public void onInitialDataLoaded() {
        Log.d("Biin", "Initial data loaded");
        HashMap<String,BNSite> sites = BNAppManager.getDataManagerInstance().getBNSites();
        HashMap<String,BNOrganization> organizations = BNAppManager.getDataManagerInstance().getBNOrganizations();
        HashMap<String,BNElement> elements = BNAppManager.getDataManagerInstance().getBNElements();
        HashMap<String,BNHighlight> highlights = BNAppManager.getDataManagerInstance().getBNHighlights();
        HashMap<String,BNCategory> categories = BNAppManager.getDataManagerInstance().getBNCategories();
        StringBuilder str = new StringBuilder();
        str.append("Initial data:\n");
        str.append("Sites: " + sites.size() + "\n");
        str.append("Organizations: " + organizations.size() + "\n");
        str.append("Elements: " + elements.size() + "\n");
        str.append("Highlights: " + highlights.size() + "\n");
        str.append("Categories: " + categories.size());
        Log.d("Biin",  str.toString());
        tvInitialData.setText(str.toString());
    }

}
