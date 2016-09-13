package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Entities.BNOrganization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNOrganizationParser {

    private static final String TAG = "BNOrganizationParser";

    private BNMediaParser mediaParser = new BNMediaParser();
    private BNLoyaltyParser loyaltyParser = new BNLoyaltyParser();

    public BNOrganization parseBNOrganization(JSONObject objectOrganization){
        BNOrganization organization = new BNOrganization();
        try{
            organization.setIdentifier(objectOrganization.getString("identifier"));
            // TODO _id
            organization.setMedia(mediaParser.parseBNMedia(objectOrganization.getJSONArray("media")));
            organization.setBrand(objectOrganization.getString("brand"));
            organization.setLoyaltyEnabled(BNUtils.getBooleanFromString(objectOrganization.getString("isLoyaltyEnabled")));
            // loyalty
            if(organization.isLoyaltyEnabled()){
                organization.setLoyalty(loyaltyParser.parseBNLoyalty(objectOrganization.getJSONObject("loyalty"), organization.getIdentifier()));
            }
            organization.setHasNPS(BNUtils.getBooleanFromString(objectOrganization.getString("hasNPS")));
            organization.setUsingBrandColors(BNUtils.getBooleanFromString(objectOrganization.getString("isUsingBrandColors")));
            organization.setPrimaryColor(BNUtils.getColorFromString(objectOrganization.getString("primaryColor")));
            organization.setSecondaryColor(BNUtils.getColorFromString(objectOrganization.getString("secondaryColor")));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return organization;
    }

    public LinkedHashMap<String, BNOrganization> parseBNOrganizations(JSONArray arrayOrganizations){
        LinkedHashMap<String, BNOrganization> result = new LinkedHashMap<>();
        try{
            for(int i = 0; i < arrayOrganizations.length(); i++){
                JSONObject objectOrganization = (JSONObject) arrayOrganizations.get(i);
                BNOrganization organization = parseBNOrganization(objectOrganization);

                result.put(organization.getIdentifier(), organization);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
