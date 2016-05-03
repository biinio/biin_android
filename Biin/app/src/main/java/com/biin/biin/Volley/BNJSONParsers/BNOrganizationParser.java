package com.biin.biin.Volley.BNJSONParsers;

import android.util.Log;

import com.biin.biin.BNUtils;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNSite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNOrganizationParser {

    private static final String TAG = "BNOrganizationParser";

    public BNOrganization parseOrganization(JSONObject objectSite){
        BNOrganization organization = new BNOrganization();
        try{
            organization.setIdentifier(objectSite.getString("identifier"));
            organization.setName(objectSite.getString("name"));
            organization.setLoyaltyEnabled(Boolean.parseBoolean(objectSite.getString("isLoyaltyEnabled")));
            organization.setHasNPS(Boolean.parseBoolean(objectSite.getString("hasNPS")));
            organization.setBrand(objectSite.getString("brand"));
            organization.setPrimaryColor(BNUtils.getColorFromString(objectSite.getString("primaryColor")));
            organization.setSecondaryColor(BNUtils.getColorFromString(objectSite.getString("secondaryColor")));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return organization;
    }

    public HashMap<String, BNOrganization> parseOrganizations(JSONArray arraySites){
        HashMap<String, BNOrganization> result = new HashMap<>();
        try{
            for(int i = 0; i < arraySites.length(); i++){
                JSONObject objectOrganization = (JSONObject) arraySites.get(i);
                BNOrganization organization = parseOrganization(objectOrganization);

                result.put(organization.getIdentifier(), organization);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
