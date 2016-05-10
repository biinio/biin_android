package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.BNUtils;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNElementParser {

    private static final String TAG = "BNElementParser";

    private BNMediaParser mediaParser = new BNMediaParser();

    private BNDataManager dataManager = BNAppManager.getDataManagerInstance();

    public BNElement parseBNElement(JSONObject objectElement){
        BNElement element = new BNElement();
        try{
            if (objectElement.has("_id")) {
                element.set_id(objectElement.getString("_id"));
            }
            if (objectElement.has("identifier")) {
                element.setIdentifier(objectElement.getString("identifier"));
            }
            //TODO shared count
            //TODO categories array
            if (objectElement.has("quantity")) {
                element.setQuantity(objectElement.getString("quantity"));
            }
            if (objectElement.has("hasQuantity")) {
                element.setHasQuantity(Boolean.parseBoolean(objectElement.getString("hasQuantity")));
            }
            if (objectElement.has("expirationDate")) {
                element.setExpirationDate(BNUtils.getDateFromString(objectElement.getString("expirationDate")));
            }
            if (objectElement.has("initialDate")) {
                element.setInitialDate(BNUtils.getDateFromString(objectElement.getString("initialDate")));
            }
            if (objectElement.has("hasTimming")) {
                element.setHasTimming(Boolean.parseBoolean(objectElement.getString("hasTimming")));
            }
            if (objectElement.has("savings")) {
                element.setSavings(objectElement.getString("savings"));
            }
            if (objectElement.has("hasSaving")) {
                element.setHasSaving(Boolean.parseBoolean(objectElement.getString("hasSaving")));
            }
            if (objectElement.has("discount")) {
                element.setDiscount(objectElement.getString("discount"));
            }
            if (objectElement.has("hasDiscount")) {
                element.setHasDiscount(Boolean.parseBoolean(objectElement.getString("hasDiscount")));
            }
            //TODO isHighlight
            if (objectElement.has("price")) {
                element.setPrice(objectElement.getString("price"));
            }
            if (objectElement.has("hasPrice")) {
                element.setHasDiscount(Boolean.parseBoolean(objectElement.getString("hasPrice")));
            }
            if (objectElement.has("listPrice")) {
                element.setListPrice(objectElement.getString("listPrice"));
            }
            if (objectElement.has("hasListPrice")) {
                element.setHasListPrice(Boolean.parseBoolean(objectElement.getString("hasListPrice")));
            }
            if (objectElement.has("hasFromPrice")) {
                element.setHasFromPrice(Boolean.parseBoolean(objectElement.getString("hasFromPrice")));
            }
            if (objectElement.has("isTaxIncludedInPrice")) {
                element.setTaxIncludedInPrice(Boolean.parseBoolean(objectElement.getString("isTaxIncludedInPrice")));
            }
//            element.setCurrency(Integer.parseInt(objectElement.getString("currencyType")));
            // TODO searchTags array
            if (objectElement.has("subTitle")) {
                element.setSubTitle(objectElement.getString("subTitle"));
            }
            if (objectElement.has("title")) {
                element.setTitle(objectElement.getString("title"));
            }
            if (objectElement.has("collectCount")) {
                element.setCollectCount(Integer.parseInt(objectElement.getString("collectCount")));
            }
            if (objectElement.has("detailsHtml")) {
                element.setDetailsHtml(objectElement.getString("detailsHtml"));
            }
            if (objectElement.has("reservedQuantity")) {
                element.setReservedQuantity(objectElement.getString("reservedQuantity"));
            }
            if (objectElement.has("claimedQuantity")) {
                element.setClaimedQuantity(objectElement.getString("claimedQuantity"));
            }
            if (objectElement.has("actualQuantity")) {
                element.setActualQuantity(objectElement.getString("actualQuantity"));
            }
            if (objectElement.has("media")) {
                element.setMedia(mediaParser.parseBNMedia(objectElement.getJSONArray("media")));
            }
            if (objectElement.has("userShared")) {
                element.setUserShared(Boolean.parseBoolean(objectElement.getString("userShared")));
            }
            if (objectElement.has("userLiked")) {
                element.setUserLiked(Boolean.parseBoolean(objectElement.getString("userLiked")));
            }
            if (objectElement.has("userCollected")) {
                element.setUserCollected(Boolean.parseBoolean(objectElement.getString("userCollected")));
            }
            if (objectElement.has("userViewed")) {
                element.setUserViewed(Boolean.parseBoolean(objectElement.getString("userViewed")));
            }
            if (objectElement.has("hasCallToAction")) {
                element.setHasCallToAction(Boolean.parseBoolean(objectElement.getString("hasCallToAction")));
            }
            if (objectElement.has("callToActionURL")) {
                element.setCallToActionURL(objectElement.getString("callToActionURL"));
            }
            if (objectElement.has("callToActionTitle")) {
                element.setCallToActionTitle(objectElement.getString("callToActionTitle"));
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return element;
    }

    public HashMap<String, BNElement> parseBNElements(JSONArray arrayElements){
        HashMap<String, BNElement> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayElements.length(); i++){
                JSONObject objectElement = (JSONObject) arrayElements.get(i);
                BNElement element = parseBNElement(objectElement);

                result.put(element.getIdentifier(), element);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

    public HashMap<String, BNElement> parseBNElementsId(JSONArray arrayElements){
        HashMap<String, BNElement> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayElements.length(); i++){
                JSONObject objectElement = (JSONObject) arrayElements.get(i);
                BNElement element = parseBNElement(objectElement);

                result.put(element.get_id(), element);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

    public BNElement cloneBNElement(JSONObject objectElement){
        BNElement element = new BNElement();
        try{
            if (objectElement.has("_id")) {
                element = dataManager.getBNElementId(objectElement.getString("_id"));
            }
            if(element == null) {
                if (objectElement.has("identifier")) {
                    BNElement clon = dataManager.getBNElement(objectElement.getString("identifier"));
                    if(clon != null) {
                        try {
                            element = (BNElement) clon.clone();
                            element.set_id(objectElement.getString("_id"));
                        } catch (CloneNotSupportedException e) {
                            Log.e(TAG, "Error clonando el objeto.", e);
                        }
                    }else{
                        Log.e(TAG, "No se obtuvo el elemento ni por _id ni por identifier.");
                    }
                }
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return element;
    }

    public HashMap<String, BNElement> cloneBNElements(JSONArray arrayElements){
        HashMap<String, BNElement> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayElements.length(); i++){
                JSONObject objectElement = (JSONObject) arrayElements.get(i);
                BNElement element = cloneBNElement(objectElement);

                result.put(element.get_id(), element);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
