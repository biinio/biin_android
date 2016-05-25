package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Entities.BNMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramirezallan on 5/9/16.
 */
public class BNMediaParser {

    private static final String TAG = "BNMediaParser";

    public BNMedia parseBNMedia(JSONObject objectElement){
        BNMedia media = new BNMedia();
        try{
            media.setUrl(objectElement.getString("url"));
            media.setVibrantColor(BNUtils.getColorFromString(objectElement.getString("vibrantColor")));
            media.setVibrantDarkColor(BNUtils.getColorFromString(objectElement.getString("vibrantDarkColor")));
            media.setVibrantLightColor(BNUtils.getColorFromString(objectElement.getString("vibrantLightColor")));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return media;
    }

    public List<BNMedia> parseBNMedia(JSONArray arrayMedia){
        List<BNMedia> result = new ArrayList<>();
        try{
            for(int i = 0; i < arrayMedia.length(); i++){
                JSONObject objectMedia = (JSONObject) arrayMedia.get(i);
                BNMedia media = parseBNMedia(objectMedia);

                result.add(media);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }
}
