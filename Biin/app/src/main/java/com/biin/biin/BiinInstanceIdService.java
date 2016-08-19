package com.biin.biin;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramirezallan on 8/9/16.
 */
public class BiinInstanceIdService extends FirebaseInstanceIdService implements Response.Listener<JSONObject> {

    private static final String TAG = "BiinInstanceIdService";
    private static final String TOPIC = "biin";
    private String token = "";

    /**
     * The Application's current Instance ID token is no longer valid
     * and thus a new one must be requested.
     */
    @Override
    public void onTokenRefresh() {
        // If you need to handle the generation of a token, initially or
        // after a refresh this is where you should do that.
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);

        // Once a token is generated, we subscribe to topic.
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
    }

    private void sendRegistrationToServer(String token) {
        this.token = token;
        Log.e(TAG, "Biin FCM Token: " + token);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = new JSONObject();
            model.put("tokenId", token);
            model.put("platform", "android");
            request.put("model", model);
        }catch (JSONException e){
            Log.e(TAG, "Error:" + e.getMessage());
        }
        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();
        String identifier = "";

        if(biinie != null && !biinie.getIdentifier().isEmpty()){
            identifier = biinie.getIdentifier();
        }else {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
            identifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.PUT,
            BNAppManager.getInstance().getNetworkManagerInstance().getTokenRegisterUrl(identifier),
            request,
            this,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onVolleyError(error);
                }
            });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e(TAG, "Response:" + response.toString());
        try {
            String result = response.getString("result");
            String status = response.getString("status");
            if(status.equals("0") && result.equals("1")){
                SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BNUtils.BNStringExtras.FCMToken, token);
                editor.commit();
            }else{
                Log.e(TAG, "Error enviando el token.");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }
    }
}