package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNBiiniesListener;
import com.biin.biin.Volley.Listeners.BNLoginListener;

public class LoginActivity extends AppCompatActivity implements BNLoginListener.IBNLoginListener, BNBiiniesListener.IBNBiiniesListener {

    private static final String TAG = "LoginActivity";

    private BNLoginListener loginListener;
    private BNBiiniesListener biiniesListener;

    private TextView tvLoginTitle, tvLoginHint, tvLoginBiin;
    private FormEditText etEmail, etPassword;
    private ProgressBar pbLogin;

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tvLoginBiin.setOnClickListener(null);
            pbLogin.setVisibility(View.VISIBLE);
            logIn();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpScreen();
    }

    private void setUpScreen(){
        Typeface lato_light = BNUtils.getLato_light();
        Typeface lato_regular = BNUtils.getLato_regular();

        tvLoginTitle = (TextView)findViewById(R.id.tvLoginTitle);
        tvLoginHint = (TextView)findViewById(R.id.tvLoginHint);
        tvLoginBiin = (TextView)findViewById(R.id.tvLoginBiin);
        etEmail = (FormEditText)findViewById(R.id.etLoginEmail);
        etPassword = (FormEditText)findViewById(R.id.etLoginPass);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);

        tvLoginTitle.setTypeface(lato_regular);
        tvLoginHint.setTypeface(lato_regular);
        tvLoginBiin.setTypeface(lato_regular);
        etEmail.setTypeface(lato_light);
        etPassword.setTypeface(lato_light);

        tvLoginTitle.setLetterSpacing(0.3f);

        int etEmailPaddingLeft = etEmail.getPaddingLeft();
        int etEmailPaddingTop = etEmail.getPaddingTop();
        int etEmailPaddingRight = etEmail.getPaddingRight();
        int etEmailPaddingBottom = etEmail.getPaddingBottom();

        etEmail.setBackgroundResource(R.color.colorAccent);
        etEmail.setPadding(etEmailPaddingLeft, etEmailPaddingTop, etEmailPaddingRight, etEmailPaddingBottom);

        int etPasswordPaddingLeft = etPassword.getPaddingLeft();
        int etPasswordPaddingTop = etPassword.getPaddingTop();
        int etPasswordPaddingRight = etPassword.getPaddingRight();
        int etPasswordPaddingBottom = etPassword.getPaddingBottom();

        etPassword.setBackgroundResource(R.color.colorAccent);
        etPassword.setPadding(etPasswordPaddingLeft, etPasswordPaddingTop, etPasswordPaddingRight, etPasswordPaddingBottom);

        tvLoginBiin.setOnClickListener(loginClick);

        findViewById(R.id.ivLoginBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToSignUp();
            }
        });
    }

    private void logIn(){
        if(checkFields()){
            loginRequest();
        }else{
            tvLoginBiin.setOnClickListener(loginClick);
            pbLogin.setVisibility(View.GONE);
        }
    }

    private void loginRequest(){
        loginListener = new BNLoginListener();
        loginListener.setListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getNetworkManagerInstance().getAuthUrl(etEmail.getText().toString(), etPassword.getText().toString()),
                null,
                loginListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "Login");
    }

    private boolean checkFields(){
        boolean allValid = true;
        FormEditText[] allFields = { etEmail, etPassword };

        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        return allValid;
    }

    @Override
    public void onLoginResponse(String identifier) {
        if(!identifier.isEmpty()){
            getBiinie(identifier);
        }else{
            Log.e(TAG, "Error: no se obtuvieron datos");
            Toast.makeText(this, getString(R.string.BadEmail), Toast.LENGTH_LONG).show();
            tvLoginBiin.setOnClickListener(loginClick);
            pbLogin.setVisibility(View.GONE);
        }
    }

    private void getBiinie(final String identifier){
        biiniesListener = new BNBiiniesListener();
        biiniesListener.setListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getNetworkManagerInstance().getUrlBiinie(identifier),
                null,
                biiniesListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "Biinie");
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
        tvLoginBiin.setOnClickListener(loginClick);
        pbLogin.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.RequestFailed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        returnToSignUp();
    }

    private void returnToSignUp(){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBiiniesLoaded() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BNUtils.BNStringExtras.BNBiinie, BNAppManager.getDataManagerInstance().getBiinie().getIdentifier());
        editor.commit();

        Log.e(TAG, "Biinie cargado correctamente");

        Intent i = new Intent(LoginActivity.this, PrivacyActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBiinieError() {
        Log.e(TAG, getString(R.string.RequestFailed));
        tvLoginBiin.setOnClickListener(loginClick);
        pbLogin.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.RequestFailed), Toast.LENGTH_SHORT).show();
    }
}
