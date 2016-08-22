package com.biin.biin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.biin.biin.Entities.BNBeacon;
import com.biin.biin.Managers.BNAppManager;
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

import java.util.ArrayList;
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
                sendNotification("IBeaconRegion entered", "Region entered " + region.getIdentifier() + " (" + region.getMinor() + ", " + region.getMajor() + ")");
            }

            @Override
            public void onRegionAbandoned(IBeaconRegion region) {
                //IBeacon region has been abandoned
                Log.e("IBeaconRegion", "Region abandoned " + region.getIdentifier() + " (" + region.getMinor() + ", " + region.getMajor() + ")");
                sendNotification("IBeaconRegion abandoned", "Region abandoned " + region.getIdentifier() + " (" + region.getMinor() + ", " + region.getMajor() + ")");
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
                Log.e(TAG, "Beacon discovered");
            }
        };
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
                        .setMinor(1)//BeaconRegion.ANY_MINOR
                        .build();
                beaconRegions.add(region);
                i++;
            }
        }

        return beaconRegions;
    }

    private void sendNotification(String title, String text){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).build();

        notificationManager.notify(0, n);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
