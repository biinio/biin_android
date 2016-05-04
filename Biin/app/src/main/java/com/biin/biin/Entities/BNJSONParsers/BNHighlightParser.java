package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNHighlight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNHighlightParser {

    private static final String TAG = "BNHighlightParser";

    public BNHighlight parseBNHighlight(JSONObject objectElement){
        BNHighlight highlight = new BNHighlight();
        try{
            highlight.set_id(objectElement.getString("_id"));
            highlight.setIdentifier(objectElement.getString("identifier"));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return highlight;
    }

    public HashMap<String, BNHighlight> parseBNHighlights(JSONArray arrayHighlight){
        HashMap<String, BNHighlight> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayHighlight.length(); i++){
                JSONObject objectHighlight = (JSONObject) arrayHighlight.get(i);
                BNHighlight highlight = parseBNHighlight(objectHighlight);

                result.put(highlight.getIdentifier(), highlight);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
