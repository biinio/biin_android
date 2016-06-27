package com.biin.biin.Volley.Listeners;

import android.util.Log;

import com.android.volley.Response;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNJSONParsers.BNElementParser;
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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ramirezallan on 6/20/16.
 */
public class BNSitesListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNSitesListener";

    private IBNSitesListener listener;
    private List<BNSite> sites;
    private BNDataManager dataManager;
    private boolean isFavourites = false;
    private String identifier;

    public BNSitesListener(List<BNSite> sites, boolean isFavourites) {
        this.sites = sites;
        this.isFavourites = isFavourites;
        dataManager = BNAppManager.getDataManagerInstance();
    }

    public void setListener(IBNSitesListener listener) {
        this.listener = listener;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject("data");

            // parsear elements_by_identifier
            parseElements(data.getJSONArray("elements"));

            // parsear organizations
            parseOrganizations(data.getJSONArray("organizations"));

            // parsear showcases
            parseShowcases(data.getJSONArray("showcases"));

            sites.remove(sites.size() - 1);
            if (this.listener != null) {
                this.listener.onItemRemoved(sites.size());
            } else {
                Log.e(TAG, "El listener es nulo o no ha sido seteado.");
            }

            // parsear sites
            parseSites(data.getJSONArray("sites"));
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }

        if (this.listener != null) {
            this.listener.onLoadMoreSitesResponse();
        } else {
            Log.e(TAG, "El listener es nulo o no ha sido seteado.");
        }
    }

    private void parseElements(JSONArray arrayElements){
        BNElementParser elementParser = new BNElementParser();
        LinkedHashMap<String, BNElement> result = elementParser.parseBNElements(arrayElements);
        // agregar el resultado de elements al data manager
        dataManager.addBNElements(result);
    }

    private void parseOrganizations(JSONArray arrayOrganizations){
        BNOrganizationParser organizationParser = new BNOrganizationParser();
        LinkedHashMap<String, BNOrganization> result = organizationParser.parseBNOrganizations(arrayOrganizations);
        // agregar el resultado de organizations al data manager
        dataManager.addBNOrganizations(result);
    }

    private void parseShowcases(JSONArray arrayShowcases){
        BNShowcaseParser showcaseParser = new BNShowcaseParser();
        LinkedHashMap<String, BNShowcase> result = showcaseParser.parseBNShowcases(arrayShowcases);
        // agregar el resultado de showcases al data manager
        dataManager.addBNShowcases(result);
    }

    private void parseSites(JSONArray arraySites){
        BNSiteParser siteParser = new BNSiteParser();

        try {
            for(int i = 0; i < arraySites.length(); i++){
                JSONObject objectSite = (JSONObject) arraySites.get(i);
                BNSite site = siteParser.parseBNSite(objectSite);
                if(this.identifier == null || site.getOrganizationIdentifier().equals(this.identifier)) {
                    sites.add(site);
//                    dataManager.addNearByBNSite(site);

                    if (site.isUserLiked()) {
                        dataManager.addFavouriteBNSite(site);
                    }

                    if (this.listener != null && (!isFavourites || site.isUserLiked())) {
                        this.listener.onItemInserted(sites.size());
                    } else {
                        Log.e(TAG, "El listener es nulo o no ha sido seteado.");
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }
    }

    public interface IBNSitesListener {
        void onItemRemoved(int position);
        void onItemInserted(int position);
        void onLoadMoreSitesResponse();
    }

}
