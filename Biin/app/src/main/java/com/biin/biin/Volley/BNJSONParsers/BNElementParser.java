package com.biin.biin.Volley.BNJSONParsers;

import android.util.Log;

import com.biin.biin.BNUtils;
import com.biin.biin.Entities.BNElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNElementParser {

    private static final String TAG = "BNElementParser";

    public BNElement parseElement(JSONObject objectElement){
        BNElement element = new BNElement();
        try{
            element.set_id(objectElement.getString("_id"));
            element.setIdentifier(objectElement.getString("identifier"));
            //TODO shared count
            //TODO categories array
            element.setQuantity(objectElement.getString("quantity"));
            element.setHasQuantity(Boolean.parseBoolean(objectElement.getString("hasQuantity")));
            element.setExpirationDate(BNUtils.getDateFromString(objectElement.getString("expirationDate")));
            element.setInitialDate(BNUtils.getDateFromString(objectElement.getString("initialDate")));
            element.setHasTimming(Boolean.parseBoolean(objectElement.getString("hasTimming")));
            element.setSavings(objectElement.getString("savings"));
            element.setHasSaving(Boolean.parseBoolean(objectElement.getString("hasSaving")));
            element.setDiscount(objectElement.getString("discount"));
            element.setHasDiscount(Boolean.parseBoolean(objectElement.getString("hasDiscount")));
            //TODO isHighlight
            element.setPrice(objectElement.getString("price"));
            element.setHasDiscount(Boolean.parseBoolean(objectElement.getString("hasPrice")));
            element.setListPrice(objectElement.getString("listPrice"));
            element.setHasListPrice(Boolean.parseBoolean(objectElement.getString("hasListPrice")));
            element.setHasFromPrice(Boolean.parseBoolean(objectElement.getString("hasFromPrice")));
            element.setTaxIncludedInPrice(Boolean.parseBoolean(objectElement.getString("isTaxIncludedInPrice")));
//            element.setCurrency(Integer.parseInt(objectElement.getString("currencyType")));
            //TODO searchTags array
            element.setSubTitle(objectElement.getString("subTitle"));
            element.setTitle(objectElement.getString("title"));
            element.setCollectCount(Integer.parseInt(objectElement.getString("collectCount")));
            element.setDetailsHtml(objectElement.getString("detailsHtml"));
            element.setReservedQuantity(objectElement.getString("reservedQuantity"));
            element.setClaimedQuantity(objectElement.getString("claimedQuantity"));
            element.setActualQuantity(objectElement.getString("actualQuantity"));
            //TODO media array
            element.setUserShared(Boolean.parseBoolean(objectElement.getString("userShared")));
            element.setUserLiked(Boolean.parseBoolean(objectElement.getString("userLiked")));
            element.setUserCollected(Boolean.parseBoolean(objectElement.getString("userCollected")));
            element.setUserViewed(Boolean.parseBoolean(objectElement.getString("userViewed")));
            element.setHasCallToAction(Boolean.parseBoolean(objectElement.getString("hasCallToAction")));
            element.setCallToActionURL(objectElement.getString("callToActionURL"));
            element.setCallToActionTitle(objectElement.getString("callToActionTitle"));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }catch (NumberFormatException e){
            Log.e(TAG, "Error en el formato numérico.", e);
        }
        return element;
    }

    public HashMap<String, BNElement> parseElements(JSONArray arrayElements){
        HashMap<String, BNElement> result = new HashMap<>();
        try{
            for(int i = 0; i < arrayElements.length(); i++){
                JSONObject objectElement = (JSONObject) arrayElements.get(i);
                BNElement element = parseElement(objectElement);

                result.put(element.getIdentifier(), element);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
