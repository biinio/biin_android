package com.biin.biin.Entities.Listeners;

import android.util.Log;

import com.android.volley.Response;
import com.biin.biin.Entities.BNJSONParsers.BiinieParser;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ramirezallan on 5/4/16.
 */
public class BNBiiniesListener implements Response.Listener<JSONObject> {

    private static final String TAG = "BNBiiniesListener";

    private IBNBiiniesListener listener;

    private BNDataManager dataManager = BNAppManager.getDataManagerInstance();

    public void setListener(IBNBiiniesListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        try{
            JSONObject data = response.getJSONObject("data");
            // parsear biinie
            parseBiinie(data);
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }

        if(this.listener != null) {
            this.listener.onBiiniesLoaded();
        }else{
            Log.e(TAG, "El listener es nulo o no ha sido seteado.");
        }
    }

    private void parseBiinie(JSONObject objectBiinie){
        BiinieParser biinieParser = new BiinieParser();
        Biinie result = biinieParser.parseBiinie(objectBiinie);
        // guardar el resultado del biinie en el data manager
        dataManager.setBiinie(result);
    }

    public interface IBNBiiniesListener {
        void onBiiniesLoaded();
    }

}
