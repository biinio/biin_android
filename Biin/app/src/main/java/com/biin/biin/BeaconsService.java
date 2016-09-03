package com.biin.biin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.BNBeacon;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.ScanMode;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.SpaceListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BeaconsService extends Service {

    private static final String TAG = "BeaconsService";

    private ProximityManagerContract proximityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        proximityManager = new ProximityManager(this);
        proximityManager.configuration().scanMode(ScanMode.LOW_POWER);
        proximityManager.configuration().scanPeriod(ScanPeriod.MONITORING);
        proximityManager.setSpaceListener(createSpaceListener());
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.spaces().iBeaconRegions(createIBeaconRegions());
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Service stopped");
        proximityManager.stopScanning();
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Service started");
        if (isBluetoothEnabled()) {
            startScanning();
        } else {
            Log.e(TAG, "Bluetooth disabled, stop service");
            stopSelf();
        }
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private boolean isBluetoothEnabled(){
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
                Log.e("Status", "Start monitoring");
            }
        });
    }

    private SpaceListener createSpaceListener() {
        return new SpaceListener() {
            @Override
            public void onRegionEntered(IBeaconRegion region) {
                //IBeacon region has been entered
                Log.e("IBeaconRegion", "IBeaconRegion entered " + region.getIdentifier() + " (" + region.getMinor() + ", " + region.getMajor() + ")");
                sendNotification(region.getMajor(), "IBeaconRegion entered", "Region entered " + region.getIdentifier() + " (" + region.getMinor() + ", " + region.getMajor() + ")");
            }

            @Override
            public void onRegionAbandoned(IBeaconRegion region) {
                //IBeacon region has been abandoned
                Log.e("IBeaconRegion", "Region abandoned " + region.getIdentifier() + " (" + region.getMinor() + ", " + region.getMajor() + ")");
                sendNotification(region.getMajor(), "IBeaconRegion abandoned", "Region abandoned " + region.getIdentifier() + " (" + region.getMinor() + ", " + region.getMajor() + ")");
            }

            @Override
            public void onNamespaceEntered(IEddystoneNamespace namespace) {
                //Eddystone namespace has been entered
            }

            @Override
            public void onNamespaceAbandoned(IEddystoneNamespace namespace) {
                //Eddystone namespace has been abandoned
            }

        };
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                //Beacon discovered
                sendBeaconAlert(ibeacon.getMajor());
                Log.e(TAG, "Beacon discovered " + ibeacon.getMajor());
            }
        };
    }

    private void sendBeaconAlert(int major){
        BNSite site = BNAppManager.getInstance().getDataManagerInstance().getBNSiteByMajor(major);
        if (site != null && site.getIdentifier() != null) {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
            String biinieIdentifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");

            if(!biinieIdentifier.isEmpty()) {
                String timezone = BNUtils.getTimeZone();
                DateFormat date = new SimpleDateFormat(BNUtils.getBeaconDateFormat());
                String localTime = date.format(Calendar.getInstance().getTime()) + " " + timezone;

                JSONObject request = new JSONObject();
                try {
                    JSONObject model = new JSONObject();
                    model.put("timeClient", localTime);
                    request.put("model", model);
                } catch (JSONException e) {
                    Log.e(TAG, "Error:" + e.getMessage());
                }

                String url = BNAppManager.getInstance().getNetworkManagerInstance().getBeaconDetectedUrl(biinieIdentifier, site.getIdentifier());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        request,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e(TAG, "Enviada la accion de deteccion del beacon de un site");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Notification Error:" + error.getMessage());
                            }
                        });
                BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "BeaconFound");
            }
        }
    }

    private Collection<IBeaconRegion> createIBeaconRegions(){
        List<BNBeacon> nearByBeacons = BNAppManager.getInstance().getDataManagerInstance().getNearByBeacons();
        Collection<IBeaconRegion> beaconRegions = new ArrayList<>();
        int i = 0;

        for (BNBeacon beacon : nearByBeacons) {
            if(i < 20) {
                BeaconRegion region = new BeaconRegion.Builder()
                        .setIdentifier(beacon.getIdentfier())
                        .setProximity(UUID.fromString("aabbccdd-a101-b202-c303-aabbccddeeff"))
                        .setMajor(beacon.getMajor())
                        .setMinor(beacon.getMinor())
                        .build();
                beaconRegions.add(region);
                i++;
            }
        }

        return beaconRegions;
    }

    private void sendNotification(int id, String title, String text){
        if(id > 1) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(this, SignupActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification n = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true).build();

            notificationManager.notify(id, n);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
