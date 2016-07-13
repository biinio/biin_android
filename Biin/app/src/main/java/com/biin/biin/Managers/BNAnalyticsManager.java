package com.biin.biin.Managers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Entities.BiinieAction;
import com.biin.biin.Volley.Listeners.BNActionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramirezallan on 7/5/16.
 */
public class BNAnalyticsManager implements BNActionsListener.IBNActionsListener {

    private static final String TAG = "BNAnalyticsManager";
    private static BNAnalyticsManager ourInstance = new BNAnalyticsManager();

    private Biinie biinie;
    private BNActionsListener actionsListener;

    private List<BiinieAction> pendingActions;

    protected static BNAnalyticsManager getInstance() {
        return ourInstance;
    }

    private BNAnalyticsManager() {
        pendingActions = new ArrayList<>();
        actionsListener = new BNActionsListener();
        actionsListener.setListener(this);
    }

    public void setBiinie(Biinie biinie){
        this.biinie = biinie;
    }

    public void addAction(final BiinieAction action){
        String url = BNAppManager.getInstance().getNetworkManagerInstance().getActonsUrl(biinie.getIdentifier());
        Log.d(TAG, url);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = new JSONObject();
            JSONArray actionsArray = new JSONArray();
            actionsArray.put(action.getModel());
            model.put("actions", actionsArray);
            request.put("model", model);
        } catch (JSONException e) {
            Log.e(TAG, "Error:" + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                request,
                actionsListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onActionError(error, action);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "SendAction");
    }

    public JSONObject getModel() {
        JSONObject model = new JSONObject();
        try {
            JSONArray actionsArray = new JSONArray();
            for (BiinieAction action : pendingActions) {
                actionsArray.put(action.getModel());
            }
            model.put("actions", actionsArray);
        } catch (JSONException e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
        return model;
    }

    private void onActionError(VolleyError error, BiinieAction action) {
        Log.e(TAG, "Error:" + error.getMessage());
        pendingActions.add(action);
    }

    @Override
    public void onResponseOk() {

    }

    @Override
    public void onResponseError() {

    }
}
