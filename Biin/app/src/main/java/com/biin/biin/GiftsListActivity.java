package com.biin.biin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Adapters.BNGiftAdapter;
import com.biin.biin.Components.Listeners.IBNGiftActionListener;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiftsListActivity extends AppCompatActivity implements IBNGiftActionListener, Response.Listener<JSONObject> {

    private static final String TAG = "GiftsListActivity";

    private BNDataManager dataManager;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver notificationsReceiver;
    private BNGiftAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_list);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        notificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String type = intent.getStringExtra("TYPE");
                if(type.equals("GIFT")) {
                    Log.e(TAG, "Gift recibido por notificacion");
                    int position = intent.getIntExtra("POSITION", 0);
                    if(adapter != null){
                        List<BNGift> gifts = new ArrayList<>(dataManager.getBNGifts().values());
                        adapter.addItem(gifts.get(position));
                        adapter.notifyItemInserted(0);
                        RecyclerView rvSlides = (RecyclerView) findViewById(R.id.rvGiftsList);
                        rvSlides.scrollToPosition(0);
                    }else{
                        setUpList();
                    }
                }
            }
        };

        setUpScreen();
        setUpList();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvTitle = (TextView) findViewById(R.id.tvGiftsListTitle);
        tvTitle.setTypeface(lato_regular);
        tvTitle.setLetterSpacing(0.3f);

        TextView tvGifts = (TextView) findViewById(R.id.tvEmptyGifts);
        tvGifts.setTypeface(lato_regular);
        tvGifts.setLetterSpacing(0.3f);

        findViewById(R.id.ivGiftsListBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dataManager.clearGiftsBadge(getApplicationContext());
    }

    private void setUpList(){
        RecyclerView rvSlides = (RecyclerView) findViewById(R.id.rvGiftsList);
        List<BNGift> gifts = new ArrayList<>(dataManager.getBNGifts().values());
        if (gifts.size() > 0) {
            adapter = new BNGiftAdapter(this, gifts, this);
            rvSlides.setLayoutManager(new LinearLayoutManager(this));
            rvSlides.setAdapter(adapter);
        } else {
            LinearLayout vlEmptyGifts = (LinearLayout) findViewById(R.id.vlEmptyGifts);
            vlEmptyGifts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("MESSAGING_SERVICE");
        localBroadcastManager.registerReceiver(notificationsReceiver, filter);
    }

    @Override
    protected void onStop() {
        localBroadcastManager.unregisterReceiver(notificationsReceiver);
        super.onStop();
    }

    @Override
    public void onGiftDeleted(String gift, int position) {
        JSONObject request = new JSONObject();
        try {
            JSONObject model = new JSONObject();
            model.put("giftIdentifier", gift);
//            model.put("platform", "android");
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
                Request.Method.POST,
                BNAppManager.getInstance().getNetworkManagerInstance().getGiftRefuseUrl(identifier),
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
                Log.e(TAG, "Gift eliminado en el server.");
            }else{
                Log.e(TAG, "Error actualizando el gift en el server.");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }
    }
}
