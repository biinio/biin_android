package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.Entities.BNLoyaltyCard;
import com.biin.biin.Utils.BNUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramirezallan on 9/12/16.
 */
public class BNLoyaltyParser {

    private static final String TAG = "BNLoyaltyParser";

    public BNLoyalty parseBNLoyalty(JSONObject objectLoyalty, String organizationIdentifier){
        BNLoyalty loyalty = new BNLoyalty();
        try {
            loyalty.setOrganizationIdentifier(organizationIdentifier);
            loyalty.setLoyaltyCard(parseBNLoyaltyCard(objectLoyalty.getJSONObject("loyaltyCard")));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return loyalty;
    }

    public BNLoyaltyCard parseBNLoyaltyCard(JSONObject objectLoyaltyCard){
        BNLoyaltyCard loyaltyCard = new BNLoyaltyCard();
        try {
            loyaltyCard.setIdentifier(objectLoyaltyCard.getString("identifier"));
            loyaltyCard.setTitle(objectLoyaltyCard.getString("title"));
            loyaltyCard.setRule(objectLoyaltyCard.getString("rule"));
            loyaltyCard.setGoal(objectLoyaltyCard.getString("goal"));
            loyaltyCard.setCompleted(BNUtils.getBooleanFromString(objectLoyaltyCard.getString("isCompleted")));
            loyaltyCard.setBiinieEnrolled(BNUtils.getBooleanFromString(objectLoyaltyCard.getString("isBiinieEnrolled")));
            loyaltyCard.setUnavailable(BNUtils.getBooleanFromString(objectLoyaltyCard.getString("isUnavailable")));
            loyaltyCard.setElementIdentifier(objectLoyaltyCard.getString("elementIdentifier"));
            loyaltyCard.setStartDate(BNUtils.getDateFromString(objectLoyaltyCard.getString("startDate")));
            loyaltyCard.setEndDate(BNUtils.getDateFromString(objectLoyaltyCard.getString("endDate")));
            loyaltyCard.setEnrolledDate(BNUtils.getDateFromString(objectLoyaltyCard.getString("enrolledDate")));
            loyaltyCard.setSlots(Integer.parseInt(objectLoyaltyCard.getString("slots")));
            loyaltyCard.setSlotsFilled(Integer.parseInt(objectLoyaltyCard.getString("usedSlots")));
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return loyaltyCard;
    }

}
