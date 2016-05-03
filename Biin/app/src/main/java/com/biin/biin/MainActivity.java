package com.biin.biin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Managers.BNNetworkManager;
import com.biin.biin.Volley.BNJSONParsers.BNInitialDataListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BNInitialDataListener.IBNInitialDataListener {

    private BNInitialDataListener initialDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getInitialData();
    }

    private void getInitialData(){
        initialDataListener = new BNInitialDataListener();
        initialDataListener.setListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNNetworkManager.getInstance().getUrlGetInitTest(),
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
    public void onInitialDataLoaded() {
        Log.d("Biin", "Initial data loaded");
        HashMap<String,BNSite> gets = BNDataManager.getInstance().getSites();
        int count = gets.size();
        Log.d("Biin", "Initial data: " + count + " sites");
    }
}
