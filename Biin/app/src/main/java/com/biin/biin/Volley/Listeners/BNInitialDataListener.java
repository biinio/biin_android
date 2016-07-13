package com.biin.biin.Volley.Listeners;

import android.util.Log;

import com.android.volley.Response;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNJSONParsers.BNCategoryParser;
import com.biin.biin.Entities.BNJSONParsers.BNElementParser;
import com.biin.biin.Entities.BNJSONParsers.BNHighlightParser;
import com.biin.biin.Entities.BNJSONParsers.BNOrganizationParser;
import com.biin.biin.Entities.BNJSONParsers.BNShowcaseParser;
import com.biin.biin.Entities.BNJSONParsers.BNSiteParser;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNInitialDataListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNInitialDataListener";

    private IBNInitialDataListener listener;

    private BNDataManager dataManager = BNAppManager.getInstance().getDataManagerInstance();

    public void setListener(IBNInitialDataListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        try{
            JSONObject data = response.getJSONObject("data");

            // parsear elements_by_identifier
            parseElements(data.getJSONArray("elements"));

            // parsear organizations
            parseOrganizations(data.getJSONArray("organizations"));

            // parsear showcases
            parseShowcases(data.getJSONArray("showcases"));

            // parsear sites
            parseSites(data.getJSONArray("sites"));

            // parsear nearby_sites
            parseNearBySites(data.getJSONArray("nearbySites"));

            // parsear favourite sites
            parseFavouriteSites(data.getJSONObject("favorites").getJSONArray("sites"));

            // parsear categories
            parseCategories(data.getJSONArray("categories"));

            // parsear highlights
            parseHighlights(data.getJSONArray("highlights"));

            // parsear favourite elements
            parseFavouriteElements(data.getJSONObject("favorites").getJSONArray("elements"));

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
        LinkedHashMap<String, BNSite> result = siteParser.parseBNSites(arraySites);
        // guardar el resultado de sites en el data manager
        dataManager.setBNSites(result);
    }

    private void parseNearBySites(JSONArray arraySites){
        BNSiteParser siteParser = new BNSiteParser();
        LinkedHashMap<String, BNSite> result = siteParser.parseNearByBNSites(arraySites);
        // guardar el resultado de sites cercanos en el data manager
//        dataManager.setNearByBNSites(result);
        dataManager.setNearByBNSites(new ArrayList<>(result.values()));
    }

    private void parseFavouriteSites(JSONArray arraySites){
        BNSiteParser siteParser = new BNSiteParser();
        LinkedHashMap<String, BNSite> result = siteParser.parseFavouriteBNSites(arraySites);
        // guardar el resultado de sites favoritos en el data manager
//        dataManager.setFavouriteBNSites(result);
        dataManager.setFavouriteBNSites(new ArrayList<>(result.values()));
    }

    private void parseShowcases(JSONArray arrayShowcases){
        BNShowcaseParser showcaseParser = new BNShowcaseParser();
        LinkedHashMap<String, BNShowcase> result = showcaseParser.parseBNShowcases(arrayShowcases);
        // guardar el resultado de showcases en el data manager
        dataManager.setBNShowcases(result);
    }

    private void parseOrganizations(JSONArray arrayOrganizations){
        BNOrganizationParser organizationParser = new BNOrganizationParser();
        LinkedHashMap<String, BNOrganization> result = organizationParser.parseBNOrganizations(arrayOrganizations);
        // guardar el resultado de organizations en el data manager
        dataManager.setBNOrganizations(result);
    }

    private void parseElements(JSONArray arrayElements){
        BNElementParser elementParser = new BNElementParser();
        LinkedHashMap<String, BNElement> result = elementParser.parseBNElements(arrayElements);
        // guardar el resultado de elements en el data manager
        dataManager.setBNElements(result);
    }

    private void parseFavouriteElements(JSONArray arrayElements){
        BNElementParser elementParser = new BNElementParser();
        LinkedHashMap<String, BNElement> result = elementParser.parseReferenceBNElements(arrayElements);
        // guardar el resultado de elements en el data manager
        dataManager.setFavouriteBNElements(new ArrayList<>(result.values()));
    }

    private void parseHighlights(JSONArray arrayHighlights){
        BNHighlightParser highlightParser = new BNHighlightParser();
        List<BNHighlight> result = highlightParser.parseBNHighlights(arrayHighlights);
        // guardar el resultado de highlights en el data manager
        dataManager.setBNHighlights(result);
    }

    private void parseCategories(JSONArray arrayCategories){
        BNCategoryParser categoryParser = new BNCategoryParser();
        LinkedHashMap<String, BNCategory> result = categoryParser.parseBNCategories(arrayCategories);
        // guardar el resultado de categories en el data manager
        dataManager.setBNCategories(result);
    }

    public interface IBNInitialDataListener {
        void onInitialDataLoaded();
    }

}