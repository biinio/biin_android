package com.biin.biin.Volley.Listeners;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramirezallan on 6/28/16.
 */
public class BNLikesListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNLikesListener";

    private IBNLikesListener listener;

    public void setListener(IBNLikesListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String result = response.getString("result");
            String status = response.getString("status");
            if(status.equals("0") && result.equals("1")){
                listener.onLikeResponseOk();
            }else{
                listener.onLikeResponseError();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }
    }

    public interface IBNLikesListener {
        void onLikeResponseOk();
        void onLikeResponseError();
    }
}
