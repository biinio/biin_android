package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNSiteParser {

    private static final String TAG = "BNSiteParser";

    private BNMediaParser mediaParser = new BNMediaParser();

    private BNDataManager dataManager = BNAppManager.getDataManagerInstance();

    public BNSite parseBNSite(JSONObject objectSite){
        BNSite site = new BNSite();
        try{
            site.setIdentifier(objectSite.getString("identifier"));
            site.setOrganizationIdentifier(objectSite.getString("organizationIdentifier"));
            site.setOrganization(BNAppManager.getDataManagerInstance().getBNOrganization(site.getOrganizationIdentifier()));
            BNOrganization organization = site.getOrganization();
            if(organization != null){
                organization.addSite(site.getIdentifier());
            }
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
            site.setMedia(mediaParser.parseBNMedia(objectSite.getJSONArray("media")));
            // TODO neighbors array
            site.setShowcases(BNUtils.getIdentifiers(objectSite.getJSONArray("showcases")));
            site.setShowcasesSite();
            // TODO biins array
            // TODO userShared
            // TODO userFollowed
            // TODO userLiked
            site.setUserLiked(BNUtils.getBooleanFromString(objectSite.getString("userLiked")));
            // TODO siteSchedule
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return site;
    }

    public LinkedHashMap<String, BNSite> parseBNSites(JSONArray arraySites){
        LinkedHashMap<String, BNSite> result = new LinkedHashMap<>();
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

    public LinkedHashMap<String, BNSite> parseNearByBNSites(JSONArray arraySites){
        LinkedHashMap<String, BNSite> result = new LinkedHashMap<>();
        ArrayList<String> organizations = new ArrayList<>();
        try{
            for(int i = 0; i < arraySites.length(); i++){
                JSONObject objectSite = (JSONObject) arraySites.get(i);
                BNSite site = dataManager.getBNSite(objectSite.getString("identifier"));

                if(site != null && site.getOrganization() != null
                        && site.getOrganization().getIdentifier() != null
                        && !organizations.contains(site.getOrganization().getIdentifier())) {
                    result.put(site.getIdentifier(), site);
                    organizations.add(site.getOrganization().getIdentifier());
                }
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

    public LinkedHashMap<String, BNSite> parseFavouriteBNSites(JSONArray arraySites){
        LinkedHashMap<String, BNSite> result = new LinkedHashMap<>();
        try{
            for(int i = 0; i < arraySites.length(); i++){
                JSONObject objectSite = (JSONObject) arraySites.get(i);
                BNSite site = dataManager.getBNSite(objectSite.getString("identifier"));

                if(site != null) {
//                    site.set_id(objectSite.getString("_id"));
//                    site.setLikeDate(BNUtils.getDateFromString(objectSite.getString("likeDate")));
                    result.put(site.getIdentifier(), site);
                }
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
