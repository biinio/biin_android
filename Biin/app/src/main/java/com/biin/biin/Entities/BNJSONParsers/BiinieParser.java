package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.BNUtils;
import com.biin.biin.Entities.Biinie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/4/16.
 */
public class BiinieParser {

    private static final String TAG = "BiinieParser";

    public Biinie parseBiinie(JSONObject objectBiinie){
        Biinie biinie = new Biinie();
        try{
            //TODO _id
//            biinie.set_id(objectBiinie.getString("_id"));
            biinie.setIdentifier(objectBiinie.getString("identifier"));
            biinie.setFirstName(objectBiinie.getString("firstName"));
            biinie.setLastName(objectBiinie.getString("lastName"));
            biinie.setBiinName(objectBiinie.getString("biinName"));
            biinie.setGender(objectBiinie.getString("gender"));
            biinie.setFacebookAvatarUrl(objectBiinie.getString("facebookAvatarUrl"));
            //TODO facebook friends array
            //TODO categories array
            //TODO friends array
            biinie.setFollowers(Integer.parseInt(objectBiinie.getString("followers")));
            biinie.setFollowing(Integer.parseInt(objectBiinie.getString("following")));
            //TODO url
//            biinie.setUrl(objectBiinie.getString("url"));
            biinie.setEmail(objectBiinie.getString("email"));
            biinie.setBirthDate(BNUtils.getDateFromString(objectBiinie.getString("birthDate")));
            biinie.setEmailVerified(Boolean.parseBoolean(objectBiinie.getString("isEmailVerified")));
            biinie.setFacebook_id(objectBiinie.getString("facebook_id"));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return biinie;
    }

    public HashMap<String, Biinie> parseBiinies(JSONArray arrayBiinies){
        HashMap<String, Biinie> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayBiinies.length(); i++){
                JSONObject objectBiinie = (JSONObject) arrayBiinies.get(i);
                Biinie biinie = parseBiinie(objectBiinie);

                result.put(biinie.getIdentifier(), biinie);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}