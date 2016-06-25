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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ramirezallan on 6/24/16.
 */
public class BNElementsListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNElementsListener";

    private IBNElementsListener listener;
    private List<BNElement> elements;
    private BNDataManager dataManager;
    private boolean isFavourites = false;

    public BNElementsListener(List<BNElement> elements, boolean isFavourites) {
        this.elements = elements;
        this.isFavourites = isFavourites;
        dataManager = BNAppManager.getDataManagerInstance();
    }

    public void setListener(IBNElementsListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject("data");

            // parsear elements
            parseElements(data.getJSONArray("elements"));

            // parsear organizations
            parseOrganizations(data.getJSONArray("organizations"));

            // parsear showcases
            parseShowcases(data.getJSONArray("showcases"));

            // parsear sites
            parseSites(data.getJSONArray("sites"));

            elements.remove(elements.size() - 1);
            if (this.listener != null) {
                this.listener.onItemRemoved(elements.size());
            } else {
                Log.e(TAG, "El listener es nulo o no ha sido seteado.");
            }

            // parsear elements por categoria
            parseElementsForCategory(data.getJSONArray("elementsForCategory"));

        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }

        if (this.listener != null) {
            this.listener.onLoadMoreElementsResponse();
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
        BNSiteParser sitesParser = new BNSiteParser();
        LinkedHashMap<String, BNSite> result = sitesParser.parseBNSites(arraySites);
        // agregar el resultado de sites al data manager
        dataManager.addBNSites(result);
    }

    private void parseElementsForCategory(JSONArray arrayElements){
        BNElementParser elementParser = new BNElementParser();
        List<BNElement> cateogryElements = new ArrayList<>(elementParser.parseReferenceBNElements(arrayElements).values());

        for(int i = 0; i < cateogryElements.size(); i++){
            BNElement element = cateogryElements.get(i);
            elements.add(element);
            dataManager.addBNElement(element);

            if(element.isUserLiked()){
                dataManager.addFavouriteBNElement(element);
            }

            if (this.listener != null && (!isFavourites || element.isUserLiked())) {
                this.listener.onItemInserted(elements.size());
            } else {
                Log.e(TAG, "El listener es nulo o no ha sido seteado.");
            }
        }
    }

    public interface IBNElementsListener {
        void onItemRemoved(int position);
        void onItemInserted(int position);
        void onLoadMoreElementsResponse();
    }

}
