package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Entities.BNShowcase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/9/16.
 */
public class BNShowcaseParser {

    private static final String TAG = "BNShowcaseParser";

    private BNElementParser elementParser = new BNElementParser();

    public BNShowcase parseBNShowcase(JSONObject objectShowcase){
        BNShowcase showcase = new BNShowcase();
        try{
//            showcase.set_id(objectShowcase.getString("_id"));
            showcase.setIdentifier(objectShowcase.getString("identifier"));
//            showcase.setSubTitle(objectShowcase.getString("subTitle"));
            showcase.setTitle(objectShowcase.getString("title"));
//            showcase.setElements_quantity(Integer.parseInt(objectShowcase.getString("elements_quantity")));
            showcase.setElements(BNUtils.getIdentifiers(objectShowcase.getJSONArray("elements")));
//            showcase.setElements(new ArrayList<>(elementParser.cloneBNElements(objectShowcase.getJSONArray("elements")).values()));
            showcase.setElementsShowcase();
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return showcase;
    }

    public HashMap<String, BNShowcase> parseBNShowcases(JSONArray arrayShowcases){
        HashMap<String, BNShowcase> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayShowcases.length(); i++){
                JSONObject objectShowcase = (JSONObject) arrayShowcases.get(i);
                BNShowcase showcase = parseBNShowcase(objectShowcase);

                result.put(showcase.getIdentifier(), showcase);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
