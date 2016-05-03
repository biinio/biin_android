package com.biin.biin.Volley.BNJSONParsers;

import android.util.Log;

import com.biin.biin.BNUtils;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNCategoryParser {

    private static final String TAG = "BNCategoryParser";

    public BNCategory parseCategory(JSONObject objectElement){
        BNCategory category = new BNCategory();
        try{
            category.setIdentifier(objectElement.getString("identifier"));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return category;
    }

    public HashMap<String, BNCategory> parseCategories(JSONArray arrayCategory){
        HashMap<String, BNCategory> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayCategory.length(); i++){
                JSONObject objectCategory = (JSONObject) arrayCategory.get(i);
                BNCategory category = parseCategory(objectCategory);

                result.put(category.getIdentifier(), category);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
