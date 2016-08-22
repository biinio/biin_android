package com.biin.biin;

import android.bluetooth.BluetoothAdapter;
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
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNUtils;
import com.kontakt.sdk.android.ble.configuration.scan.ScanMode;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GiftsListActivity extends AppCompatActivity implements IBNGiftActionListener, Response.Listener<JSONObject> {

    private static final String TAG = "GiftsListActivity";

    private BNDataManager dataManager;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver notificationsReceiver;
    private List<BNGift> gifts;
    private BNGiftAdapter adapter;
    private ProximityManagerContract proximityManager;
    private int beaconMajor = 0;
    private double beaconDistance = 100d;
    private String organizationIdentifier = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_list);

        Intent i = new Intent(this, BeaconsService.class);
        stopService(i);

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
                        gifts = new ArrayList<>(dataManager.getBNGifts().values());
                        adapter.addItem(gifts.get(position));
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemRangeChanged(0, gifts.size());
                        RecyclerView rvSlides = (RecyclerView) findViewById(R.id.rvGiftsList);
                        rvSlides.scrollToPosition(0);
                    }else{
                        setUpList();
                    }
                    dataManager.clearGiftsBadge(getApplicationContext());
                }else{
                    if(type.equals("DELIEVRED")) {
                        String giftIdentifier = intent.getStringExtra("IDENTIFIER");
                        if(adapter != null) {
                            int position = adapter.delieverGift(giftIdentifier);
                            if(position > -1) {
                                adapter.notifyItemChanged(position);
                            }
                        }
                    }
                }
            }
        };

        setUpScreen();
        setUpList();
        startScaning();
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
        gifts = new ArrayList<>(dataManager.getBNGifts().values());
        if (gifts.size() > 0) {
            adapter = new BNGiftAdapter(this, gifts, this);
            rvSlides.setLayoutManager(new LinearLayoutManager(this));
            rvSlides.setAdapter(adapter);
        } else {
            LinearLayout vlEmptyGifts = (LinearLayout) findViewById(R.id.vlEmptyGifts);
            vlEmptyGifts.setVisibility(View.VISIBLE);
        }
    }

    private void startScaning() {
        proximityManager = new ProximityManager(this);
        proximityManager.configuration().scanMode(ScanMode.BALANCED);
        proximityManager.configuration().deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(15));
        proximityManager.setIBeaconListener(createIBeaconListener());
    }

    private IBeaconListener createIBeaconListener() {
        return new IBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice device, IBeaconRegion region) {
                Log.e("IBeacon", "IBeacon discovered: " + device.getUniqueId());
                if ((device.getDistance() + 5d) < beaconDistance) {
                    BNSite site = dataManager.getBNSiteByMajor(device.getMajor());
                    if(site != null){
                        setOrganization(device.getMajor(), device.getDistance(), site.getOrganizationIdentifier());
                    }
                }
            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                if (iBeacons.size() > 0) {
                    IBeaconDevice nearest = iBeacons.get(0);
                    boolean found = false;

                    for (IBeaconDevice device : iBeacons) {
                        if(device.getMajor() == beaconMajor){
                            beaconDistance = device.getDistance();
                            found = true;
                        }

                        if (device.getDistance() < nearest.getDistance()) {
                            if (dataManager.getBNSiteByMajor(device.getMajor()) != null) {
                                nearest = device;
                            }
                        }
                    }

                    if (!found || (nearest.getMajor() != beaconMajor && (nearest.getDistance() + 5d) <  beaconDistance)) {
                        BNSite site = dataManager.getBNSiteByMajor(nearest.getMajor());
                        if(site != null){
                            setOrganization(nearest.getMajor(), nearest.getDistance(), site.getOrganizationIdentifier());
                        }
                    }
                } else {
                    unsetOrganization();
                }
            }

            @Override
            public void onIBeaconLost(IBeaconDevice device, IBeaconRegion region) {
                Log.e("IBeacon", "IBeacon lost: " + device.getUniqueId());
                if(device.getMajor() == beaconMajor){
                    unsetOrganization();
                }
            }
        };
    }

    private void setOrganization(int beaconMajor, double beaconDistance, String organizationIdentifier){
        this.beaconMajor = beaconMajor;
        this.beaconDistance = beaconDistance;
        this.organizationIdentifier = organizationIdentifier;
        Log.e(TAG, "Current organization identifier: " + organizationIdentifier);
        if(adapter != null) {
            adapter.setOrganizationIdentifier(this.organizationIdentifier);
            adapter.notifyItemRangeChanged(0, gifts.size());
        }
//        if(gifts != null){
//            for (int i = 0; i < gifts.size(); i++) {
//                if(gifts.get(i).getOrganizationIdentifier().equals(organizationIdentifier)){
//                    adapter.notifyItemChanged(i);
//                }
//            }
//        }
    }

    private void unsetOrganization(){
        beaconMajor = 0;
        beaconDistance = 100d;
        organizationIdentifier = "";
        Log.e(TAG, "Current organization identifier: " + organizationIdentifier);
        if(adapter != null) {
            adapter.setOrganizationIdentifier(this.organizationIdentifier);
            adapter.notifyItemRangeChanged(0, gifts.size());
        }
    }

    private void startScanning() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) { //bluetooth active
            proximityManager.connect(new OnServiceReadyListener() {
                @Override
                public void onServiceReady() {
                    proximityManager.startScanning();
                    Log.e(TAG, "Start scanning");
                }
            });
        } else {
            Log.e(TAG, "Bluetooth apagado");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("MESSAGING_SERVICE");
        localBroadcastManager.registerReceiver(notificationsReceiver, filter);
        startScanning();
    }

    @Override
    protected void onStop() {
        localBroadcastManager.unregisterReceiver(notificationsReceiver);
        proximityManager.stopScanning();
        Intent i = new Intent(this, BeaconsService.class);
        startService(i);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    @Override
    public void onGiftDeleted(String gift, int position) {
        JSONObject request = new JSONObject();
        try {
            JSONObject model = new JSONObject();
            model.put("giftIdentifier", gift);
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
