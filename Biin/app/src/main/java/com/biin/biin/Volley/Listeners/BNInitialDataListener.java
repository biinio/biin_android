package com.biin.biin.Volley.Listeners;

import android.util.Log;

import com.android.volley.Response;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNInitialDataListener implements Response.Listener<JSONObject> {

    private IBNInitialDataListener listener;

    public void setListener(IBNInitialDataListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        HashMap<String, BNSite> result = new HashMap<>();
        try{
            JSONObject data = response.getJSONObject("data");
            JSONArray sites = data.getJSONArray("sites");
            for(int i = 0; i < sites.length(); i++){
                JSONObject objSite = (JSONObject) sites.get(i);
                BNSite site = new BNSite();
                site.setIdentifier(objSite.getString("identifier"));
                site.setOrganizationIdentifier(objSite.getString("organizationIdentifier"));
//                site.setBiinieProximity(Float.parseFloat(objSite.getString("proximityUUID")));
                site.setMajor(Integer.parseInt(objSite.getString("major")));
                site.setCountry(objSite.getString("country"));
                site.setState(objSite.getString("state"));
                site.setCity(objSite.getString("city"));
                site.setZipCode(objSite.getString("zipCode"));
//                site.setUbication(objSite.getString("ubication"));
                site.setTitle(objSite.getString("title"));
                site.setSubTitle(objSite.getString("subTitle"));
                site.setStreetAddress1(objSite.getString("streetAddress1"));
                site.setStreetAddress2(objSite.getString("streetAddress2"));
                site.setLatitude(Float.parseFloat(objSite.getString("latitude")));
                site.setLongitude(Float.parseFloat(objSite.getString("longitude")));
                site.setEmail(objSite.getString("email"));
                site.setNutshell(objSite.getString("nutshell"));
                site.setPhoneNumber(objSite.getString("phoneNumber"));

                result.put(site.getIdentifier(), site);
            }
        }catch (JSONException e){

        }catch (NumberFormatException e){

        }
        BNDataManager.getInstance().setSites(result);

        if(this.listener != null) {
            this.listener.onInitialDataLoaded();
        }else{
            Log.e("BiinError", "El listener es nulo o no ha sido seteado");
        }
    }

    public interface IBNInitialDataListener {
        void onInitialDataLoaded();
    }
}