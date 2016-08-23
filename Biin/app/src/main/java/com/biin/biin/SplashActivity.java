package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Volley.Listeners.BNInitialDataListener;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, BNInitialDataListener.IBNInitialDataListener, Response.Listener<JSONObject> {

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
    private String token;

    private BNInitialDataListener initialDataListener;

    private TextView tvLoading, tvReload, tvVersion;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setUpScreen();
        locationServices();
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        tvLoading = (TextView)findViewById(R.id.tvLoading);
        tvVersion = (TextView)findViewById(R.id.tvSplashVersion);
        tvReload = (TextView)findViewById(R.id.tvReload);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        tvLoading.setTypeface(lato_regular);
        tvVersion.setTypeface(lato_regular);
        tvReload.setTypeface(lato_black);

        String version = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName.trim();
        }catch (PackageManager.NameNotFoundException ex){}

        BNUtils.setVersion(version);
        tvVersion.setText(tvVersion.getText().toString() + " " + BNUtils.getVersion());
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
            Log.e(TAG, "No se tienen permisos de localizacion.");
        }else{
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            getInitialData(getLatLon());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_PERMISSION_REQUEST){
            if(permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getInitialData(getLatLon());
            }else{
                Log.e(TAG, "No se tienen permisos de localizacion.");
                tvLoading.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
            }
        }
    }

    private String getLatLon(){
        String location = "0/0";
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            location = latitude + "/" + longitude;
        } else {
            Log.e(TAG, "No se puede obtener la localizacion. Puede no estar activada en el dispositivo.");
        }
        return location;
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
            Log.e(TAG, "No se tienen permisos de localizacion. No se puede iniciar la actualizacion de localizacion.");
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void getInitialData(String location){
        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();

        initialDataListener = new BNInitialDataListener();
        initialDataListener.setListener(this);

        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        tvReload.setVisibility(View.GONE);

        String url = BNAppManager.getInstance().getNetworkManagerInstance().getUrlInitialData(biinie.getIdentifier(), location);
        Log.d(TAG, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                "",
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
        token = FirebaseInstanceId.getInstance().getToken();

        String identifier = "";
        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        String sent = preferences.getString(BNUtils.BNStringExtras.FCMToken, "");

        if(biinie != null && biinie.getIdentifier() != null && !biinie.getIdentifier().isEmpty()){
            identifier = biinie.getIdentifier();
        }else {
            identifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");
        }

        if(!token.equals(sent)) {
            sendToken(token, identifier);
        }

        goToNextActivity();
    }

    private void sendToken(String token, String identifier){
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

    private void goToNextActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void onInitialDataError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
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

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onResponse(JSONObject response) {
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
