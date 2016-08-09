package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNGift;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNGiftParser {

    private static final String TAG = "BNGiftParser";

    private BNMediaParser mediaParser = new BNMediaParser();

    private BNDataManager dataManager = BNAppManager.getInstance().getDataManagerInstance();

    public BNGift parseBNGift(JSONObject objectGift){
        BNGift gift = new BNGift();
        try{
            gift.setIdentifier(objectGift.getString("identifier"));
            gift.setElementIdentifier(objectGift.getString("productIdentifier"));
            gift.setOrganizationIdentifier(objectGift.getString("organizationIdentifier"));
//            gift.setOrganization(BNAppManager.getInstance().getDataManagerInstance().getBNOrganization(gift.getOrganizationIdentifier()));
            gift.setName(objectGift.getString("name"));
            gift.setMessage(objectGift.getString("message"));
            String status = objectGift.getString("status");
            int statusCode = BNGift.BNGiftStatus.NONE;
            if(status.equals("SENT")){
                statusCode = BNGift.BNGiftStatus.SENT;
            }
            if(status.equals("REFUSED")){
                statusCode = BNGift.BNGiftStatus.REFUSED;
            }
            if(status.equals("SHARED")){
                statusCode = BNGift.BNGiftStatus.SHARED;
            }
            if(status.equals("CLAIMED")){
                statusCode = BNGift.BNGiftStatus.CLAIMED;
            }
            if(status.equals("DELIVERED")){
                statusCode = BNGift.BNGiftStatus.DELIVERED;
            }
            gift.setStatus(statusCode);
            gift.setReceivedDate(BNUtils.getDateFromString(objectGift.getString("receivedDate")));
            gift.setExpirationDate(BNUtils.getDateFromString(objectGift.getString("expirationDate")));
            gift.setHasExpirationDate(BNUtils.getBooleanFromString(objectGift.getString("hasExpirationDate")));
            List<String> sites = new ArrayList<>();
            JSONArray arraySites = objectGift.getJSONArray("sites");
            for(int i = 0; i < arraySites.length(); i++){
                sites.add(arraySites.getString(i));
            }
            gift.setSites(sites);
            gift.setMedia(mediaParser.parseBNMedia(objectGift.getJSONArray("media")));
            gift.setPrimaryColor(BNUtils.getColorFromString(objectGift.getString("primaryColor")));
            gift.setSecondaryColor(BNUtils.getColorFromString(objectGift.getString("secondaryColor")));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return gift;
    }

    public LinkedHashMap<String, BNGift> parseBNGifts(JSONArray arraySites){
        LinkedHashMap<String, BNGift> result = new LinkedHashMap<>();
        try{
            for(int i = 0; i < arraySites.length(); i++){
                JSONObject objectSite = (JSONObject) arraySites.get(i);
                BNGift site = parseBNGift(objectSite);

                result.put(site.getIdentifier(), site);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
