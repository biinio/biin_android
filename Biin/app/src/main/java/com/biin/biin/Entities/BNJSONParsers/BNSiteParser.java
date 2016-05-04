package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNSite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNSiteParser {

    private static final String TAG = "BNSiteParser";

    public BNSite parseBNSite(JSONObject objectSite){
        BNSite site = new BNSite();
        try{
            site.setIdentifier(objectSite.getString("identifier"));
            site.setOrganizationIdentifier(objectSite.getString("organizationIdentifier"));
            // TODO proximityUUID
//            site.setBiinieProximity(Float.parseFloat(objectSite.getString("proximityUUID")));
            site.setMajor(Integer.parseInt(objectSite.getString("major")));
            site.setCountry(objectSite.getString("country"));
            site.setState(objectSite.getString("state"));
            site.setCity(objectSite.getString("city"));
            site.setZipCode(objectSite.getString("zipCode"));
            // TODO ubication
//            site.setUbication(objectSite.getString("ubication"));
            site.setTitle(objectSite.getString("title"));
            site.setSubTitle(objectSite.getString("subTitle"));
            site.setStreetAddress1(objectSite.getString("streetAddress1"));
            site.setStreetAddress2(objectSite.getString("streetAddress2"));
            site.setLatitude(Float.parseFloat(objectSite.getString("latitude")));
            site.setLongitude(Float.parseFloat(objectSite.getString("longitude")));
            site.setEmail(objectSite.getString("email"));
            site.setNutshell(objectSite.getString("nutshell"));
            site.setPhoneNumber(objectSite.getString("phoneNumber"));
            // TODO media array
            // TODO neighbors array
            // TODO showcases array
            // TODO biins array
            // TODO userShared
            // TODO userFollowed
            // TODO userLiked
            // TODO siteSchedule
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return site;
    }

    public HashMap<String, BNSite> parseBNSites(JSONArray arraySites){
        HashMap<String, BNSite> result = new HashMap<>();
        try{
            for(int i = 0; i < arraySites.length(); i++){
                JSONObject objectSite = (JSONObject) arraySites.get(i);
                BNSite site = parseBNSite(objectSite);

                result.put(site.getIdentifier(), site);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
