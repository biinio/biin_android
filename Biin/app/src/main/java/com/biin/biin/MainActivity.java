package com.biin.biin;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Volley.Links;
import com.biin.biin.Volley.Listeners.InitialDataListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getInitialData();

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String,BNSite> gets = AppManager.getInstance().getSites();
                        int count = gets.size();
                    }
                }, 12000
        );
    }

    private void getInitialData(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Links.URL_GET_INIT_TEST,
                null,
                new InitialDataListener(),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "InitialData");
    }
}
