package com.biin.biin.Volley.BNJSONParsers;

import android.util.Log;

import com.android.volley.Response;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNInitialDataListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNInitialDataListener";

    private IBNInitialDataListener listener;

    public void setListener(IBNInitialDataListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        try{
            JSONObject data = response.getJSONObject("data");
            // parsear sites
            parseSites(data.getJSONArray("sites"));
            // parsear organizations
            parseOrganizations(data.getJSONArray("organizations"));
            // parsear elements
            parseElements(data.getJSONArray("elements"));
            // parsear highlights
//            parseHighlights(data.getJSONArray("highlights"));
            // parsear categories
            parseCategories(data.getJSONArray("categories"));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }

        if(this.listener != null) {
            this.listener.onInitialDataLoaded();
        }else{
            Log.e(TAG, "El listener es nulo o no ha sido seteado.");
        }
    }

    private void parseSites(JSONArray arraySites){
        BNSiteParser siteParser = new BNSiteParser();
        HashMap<String, BNSite> result = siteParser.parseSites(arraySites);
        // guardar el resultado de sites en el data manager
        BNDataManager.getInstance().setBNSites(result);
    }

    private void parseOrganizations(JSONArray arrayOrganizations){
        BNOrganizationParser organizationParser = new BNOrganizationParser();
        HashMap<String, BNOrganization> result = organizationParser.parseOrganizations(arrayOrganizations);
        // guardar el resultado de organizations en el data manager
        BNDataManager.getInstance().setBNOrganizations(result);
    }

    private void parseElements(JSONArray arrayElements){
        BNElementParser elementParser = new BNElementParser();
        HashMap<String, BNElement> result = elementParser.parseElements(arrayElements);
        // guardar el resultado de elements en el data manager
        BNDataManager.getInstance().setBNElements(result);
    }

    private void parseHighlights(JSONArray arrayHighlights){
        BNSiteParser siteParser = new BNSiteParser();
        HashMap<String, BNSite> result = siteParser.parseSites(arrayHighlights);
        // guardar el resultado de highlights en el data manager
        BNDataManager.getInstance().setBNSites(result);
    }

    private void parseCategories(JSONArray arrayCategories){
        BNSiteParser siteParser = new BNSiteParser();
        HashMap<String, BNSite> result = siteParser.parseSites(arrayCategories);
        // guardar el resultado de categories en el data manager
        BNDataManager.getInstance().setBNSites(result);
    }

    public interface IBNInitialDataListener {
        void onInitialDataLoaded();
    }
}