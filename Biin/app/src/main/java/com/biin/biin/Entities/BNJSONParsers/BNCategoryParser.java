package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Managers.BNAppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNCategoryParser {

    private static final String TAG = "BNCategoryParser";

    public BNCategory parseBNCategory(JSONObject objectCategory){
        BNCategory category = new BNCategory();
        try{
            category.setIdentifier(objectCategory.getString("identifier"));
            category.setElements(new ArrayList<>(getBNElements(objectCategory.getJSONArray("elements")).values()));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return category;
    }

    public LinkedHashMap<String, BNCategory> parseBNCategories(JSONArray arrayCategory){
        LinkedHashMap<String, BNCategory> result = new LinkedHashMap<>();
        try{
            for(int i = 0; i < arrayCategory.length(); i++){
                JSONObject objectCategory = (JSONObject) arrayCategory.get(i);
                BNCategory category = parseBNCategory(objectCategory);

                result.put(category.getIdentifier(), category);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

    public LinkedHashMap<String, BNElement> getBNElements(JSONArray arrayElements){
        LinkedHashMap<String, BNElement> elements = BNAppManager.getInstance().getDataManagerInstance().getBNElements();
        LinkedHashMap<String, BNElement> result = new LinkedHashMap<>();
        try{
            for(int i = 0; i < arrayElements.length(); i++){
                JSONObject objectElement = (JSONObject) arrayElements.get(i);
                BNElement element = elements.get(objectElement.getString("identifier"));

                if(element != null) {
                    element.setShowcase(BNAppManager.getInstance().getDataManagerInstance().getBNShowcase(objectElement.getString("showcaseIdentifier")));
                    element.getShowcase().setSite(BNAppManager.getInstance().getDataManagerInstance().getBNSite(objectElement.getString("siteIdentifier")));
                    result.put(element.getIdentifier(), element);
                }
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
