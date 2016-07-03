package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

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
                element.setHasQuantity(BNUtils.getBooleanFromString(objectElement.getString("hasQuantity")));
            }
            if (objectElement.has("expirationDate")) {
                element.setExpirationDate(BNUtils.getDateFromString(objectElement.getString("expirationDate")));
            }
            if (objectElement.has("initialDate")) {
                element.setInitialDate(BNUtils.getDateFromString(objectElement.getString("initialDate")));
            }
            if (objectElement.has("hasTimming")) {
                element.setHasTimming(BNUtils.getBooleanFromString(objectElement.getString("hasTimming")));
            }
            if (objectElement.has("savings")) {
                element.setSavings(objectElement.getString("savings"));
            }
            if (objectElement.has("hasSaving")) {
                element.setHasSaving(BNUtils.getBooleanFromString(objectElement.getString("hasSaving")));
            }
            if (objectElement.has("discount")) {
                element.setDiscount(objectElement.getString("discount"));
            }
            if (objectElement.has("hasDiscount")) {
                element.setHasDiscount(BNUtils.getBooleanFromString(objectElement.getString("hasDiscount")));
            }
            //TODO isHighlight
            if (objectElement.has("price")) {
                element.setPrice(objectElement.getString("price"));
            }
            if (objectElement.has("hasPrice")) {
                element.setHasDiscount(BNUtils.getBooleanFromString(objectElement.getString("hasPrice")));
            }
            if (objectElement.has("listPrice")) {
                element.setListPrice(objectElement.getString("listPrice"));
            }
            if (objectElement.has("hasListPrice")) {
                element.setHasListPrice(BNUtils.getBooleanFromString(objectElement.getString("hasListPrice")));
            }
            if (objectElement.has("hasFromPrice")) {
                element.setHasFromPrice(BNUtils.getBooleanFromString(objectElement.getString("hasFromPrice")));
            }
            if (objectElement.has("isTaxIncludedInPrice")) {
                element.setTaxIncludedInPrice(BNUtils.getBooleanFromString(objectElement.getString("isTaxIncludedInPrice")));
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
                element.setUserShared(BNUtils.getBooleanFromString(objectElement.getString("userShared")));
            }
            if (objectElement.has("userLiked")) {
                element.setUserLiked(BNUtils.getBooleanFromString(objectElement.getString("userLiked")));
            }
            if (objectElement.has("userCollected")) {
                element.setUserCollected(BNUtils.getBooleanFromString(objectElement.getString("userCollected")));
            }
            if (objectElement.has("userViewed")) {
                element.setUserViewed(BNUtils.getBooleanFromString(objectElement.getString("userViewed")));
            }
            if (objectElement.has("hasCallToAction")) {
                element.setHasCallToAction(BNUtils.getBooleanFromString(objectElement.getString("hasCallToAction")));
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

    public LinkedHashMap<String, BNElement> parseBNElements(JSONArray arrayElements){
        LinkedHashMap<String, BNElement> result = new LinkedHashMap<>();
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

    public LinkedHashMap<String, BNElement> parseReferenceBNElements(JSONArray arrayElements){
        LinkedHashMap<String, BNElement> result = new LinkedHashMap<>();
        try{
            for(int i = 0; i < arrayElements.length(); i++){
                JSONObject objectElement = (JSONObject) arrayElements.get(i);
                BNElement element = dataManager.getBNElement(objectElement.getString("identifier"));
                BNShowcase showcase = dataManager.getBNShowcase(objectElement.getString("showcaseIdentifier"));
                BNSite site = dataManager.getBNSite(objectElement.getString("siteIdentifier"));

                if(element != null && showcase != null && site != null) {
                    showcase.setSite(site);
                    element.setShowcase(showcase);
                    result.put(element.getIdentifier(), element);
                }
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
