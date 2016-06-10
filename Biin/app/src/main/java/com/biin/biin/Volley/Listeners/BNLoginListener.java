package com.biin.biin.Volley.Listeners;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramirezallan on 6/9/16.
 */
public class BNLoginListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNLoginListener";

    private IBNLoginListener listener;

    public void setListener(IBNLoginListener listener) {
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
            this.listener.onLoginResponse(identifier);
        }else{
            Log.e(TAG, "El listener es nulo o no ha sido seteado.");
        }
    }

    public interface IBNLoginListener {
        void onLoginResponse(String identifier);
    }

}
