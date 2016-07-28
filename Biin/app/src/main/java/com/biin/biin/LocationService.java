package com.biin.biin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.biin.biin.Managers.BNAppManager;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private static final int LOCATION_INTERVAL = 60000;
    private static final float LOCATION_DISTANCE = 5000f;
//    private static final float LOCATION_DISTANCE = 10f;

    private LocalBroadcastManager localBroadcastManager;
    private LocationManager locationManager = null;

    private class LocationListener implements android.location.LocationListener {
        private Location lastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            lastLocation.set(location);
            if(BNAppManager.getInstance().getPositionManagerInstance().setLastLocation(location)) {
                locationChanged();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] locationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "Error: fail to request location update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "Error: network provider does not exist" + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "Error: fail to request location update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "Error: gps provider does not exist " + ex.getMessage());
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (locationManager != null) {
            for (LocationListener locationListener : locationListeners) {
                try {
                    locationManager.removeUpdates(locationListener);
                } catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "Error: fail to remove location listners", ex);
                } catch (Exception ex) {
                    Log.i(TAG, "Error: fail to remove location listners", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void locationChanged(){
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent("LOCATION_SERVICE");
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

}
