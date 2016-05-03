package com.biin.biin.Volley.BNJSONParsers;

import android.util.Log;

import com.biin.biin.BNUtils;
import com.biin.biin.Entities.BNOrganization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNOrganizationParser {

    private static final String TAG = "BNOrganizationParser";

    public BNOrganization parseOrganization(JSONObject objectOrganization){
        BNOrganization organization = new BNOrganization();
        try{
            organization.setIdentifier(objectOrganization.getString("identifier"));
            // TODO _id
            // TODO media array
            organization.setExtraInfo(objectOrganization.getString("extraInfo"));
            organization.setDescription(objectOrganization.getString("description"));
            organization.setBrand(objectOrganization.getString("brand"));
            organization.setName(objectOrganization.getString("name"));
            organization.setLoyaltyEnabled(Boolean.parseBoolean(objectOrganization.getString("isLoyaltyEnabled")));
            // TODO loyalty
            organization.setHasNPS(Boolean.parseBoolean(objectOrganization.getString("hasNPS")));
            organization.setUsingBrandColors(Boolean.parseBoolean(objectOrganization.getString("isUsingBrandColors")));
            organization.setPrimaryColor(BNUtils.getColorFromString(objectOrganization.getString("primaryColor")));
            organization.setSecondaryColor(BNUtils.getColorFromString(objectOrganization.getString("secondaryColor")));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return organization;
    }

    public HashMap<String, BNOrganization> parseOrganizations(JSONArray arrayOrganizations){
        HashMap<String, BNOrganization> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayOrganizations.length(); i++){
                JSONObject objectOrganization = (JSONObject) arrayOrganizations.get(i);
                BNOrganization organization = parseOrganization(objectOrganization);

                result.put(organization.getIdentifier(), organization);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
