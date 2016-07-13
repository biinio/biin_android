package com.biin.biin.Volley.Listeners;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramirezallan on 7/12/16.
 */
public class BNActionsListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNActionsListener";

    private IBNActionsListener listener;

    public void setListener(IBNActionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String result = response.getString("result");
            String status = response.getString("status");
            if(status.equals("0") && result.equals("1")){
                listener.onResponseOk();
            }else{
                listener.onResponseError();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }
    }

    public interface IBNActionsListener {
        void onResponseOk();
        void onResponseError();
    }
}
