package com.biin.biin;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.Listeners.BNInitialDataListener;
import com.biin.biin.Managers.BNAppManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, BNInitialDataListener.IBNInitialDataListener{

    private static final String TAG = "SplashActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 100; // 100 meters
    private static int REMOTENESS = 1000; // 1 km

    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private boolean locationUpdates = false;
    private LocationRequest locationRequest;

    private BNInitialDataListener initialDataListener;

    private TextView tvLoading, tvReload;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
        Typeface lato_black = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");

        tvLoading = (TextView)findViewById(R.id.tvLoading);
        tvReload = (TextView)findViewById(R.id.tvReload);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        tvLoading.setTypeface(lato_regular);
        tvReload.setTypeface(lato_black);

//        getInitialData("9.73854872449546/-83.9987999326416");
        locationServices();
    }

    private void locationServices(){
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    private void measureLocation(Location location) {
        if(lastLocation.distanceTo(location) > REMOTENESS) {
            lastLocation = location;
            getDataByLocation();
        }
    }

    private void getDataByLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_REQUEST);
            Toast.makeText(this, "Don't have permissions", Toast.LENGTH_SHORT).show();
        }else{
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }

        getInitialData(getLatLon());
    }

    private String getLatLon(){
        String loc = "0,0";
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            loc = latitude + "/" + longitude;
        } else {
            Toast.makeText(this, "Can't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
        }
        return loc;
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FATEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_REQUEST);
            Toast.makeText(this, "Don't have permissions", Toast.LENGTH_SHORT).show();
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Toast.makeText(this, "Location updates started", Toast.LENGTH_SHORT).show();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void getInitialData(String location){
        initialDataListener = new BNInitialDataListener();
        initialDataListener.setListener(this);

        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        tvReload.setVisibility(View.GONE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getNetworkManagerInstance().getUrlGetInitialDataTest() + location,
                null,
                initialDataListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onInitialDataError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "InitialData");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (googleApiClient.isConnected() && locationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getDataByLocation();
        if (locationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed, error code: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        measureLocation(location);
    }

    @Override
    public void onInitialDataLoaded() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        Toast.makeText(this, "Listo", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onInitialDataError(VolleyError error){
        Log.e(TAG, error.getMessage());
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

        tvLoading.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        tvReload.setVisibility(View.VISIBLE);

        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInitialData(getLatLon());
            }
        });
    }

}