package com.biin.biin.Volley.Listeners;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramirezallan on 6/9/16.
 */
public class BNSignupListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNSignupListener";

    private IBNSignupListener listener;

    public void setListener(IBNSignupListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        String identifier = "";
        try{
            JSONObject data = response.getJSONObject("data");
            identifier = data.getString("identifier");
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }

        if(this.listener != null) {
            this.listener.onSignupResponse(identifier);
        }else{
            Log.e(TAG, "El listener es nulo o no ha sido seteado.");
        }
    }

    public interface IBNSignupListener {
        void onSignupResponse(String identifier);
    }

}
