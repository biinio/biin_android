package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNCategoryParser {

    private static final String TAG = "BNCategoryParser";

    private BNElementParser elementParser = new BNElementParser();

    public BNCategory parseBNCategory(JSONObject objectCategory){
        BNCategory category = new BNCategory();
        try{
            category.setIdentifier(objectCategory.getString("identifier"));
            category.setElements(new ArrayList<>(elementParser.cloneBNElements(objectCategory.getJSONArray("elements")).values()));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return category;
    }

    public HashMap<String, BNCategory> parseBNCategories(JSONArray arrayCategory){
        HashMap<String, BNCategory> result = new HashMap<>();
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

}
