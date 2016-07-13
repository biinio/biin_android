package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNBiiniesListener;

public class SignupActivity extends AppCompatActivity implements BNBiiniesListener.IBNBiiniesListener {

    private static final String TAG = "SignupActivity";

    private BNBiiniesListener biiniesListener;

    private TextView tvOptionLogin, tvSignupFb, tvSignupBiin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyBiinie();
    }

    private void verifyBiinie(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        String identifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");

        if(identifier.isEmpty()){
            setUpScreen();
        }else{
            getBiinie(identifier);
        }
    }

    private void getBiinie(final String identifier){
        biiniesListener = new BNBiiniesListener();
        biiniesListener.setListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getInstance().getNetworkManagerInstance().getUrlBiinie(identifier),
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

    @Override
    public void onBiiniesLoaded() {
        Log.e(TAG, "Biinie cargado correctamente");

        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBiinieError() {
        Log.e(TAG, "Error cargando biinie");
        setUpScreen();
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
        setUpScreen();
    }

    private void setUpScreen(){
        setContentView(R.layout.activity_signup);

        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        TextView tvWelcome1 = (TextView)findViewById(R.id.tvWelcome1);
        TextView tvWelcome2 = (TextView)findViewById(R.id.tvWelcome2);

        tvSignupFb = (TextView)findViewById(R.id.tvSignupFb);
        tvSignupBiin = (TextView)findViewById(R.id.tvSignupBiin);
        tvOptionLogin = (TextView)findViewById(R.id.tvOptionLogin);

        tvWelcome1.setTypeface(lato_regular);
        tvWelcome2.setTypeface(lato_regular);
        tvSignupFb.setTypeface(lato_black);
        tvSignupBiin.setTypeface(lato_black);
        tvOptionLogin.setTypeface(lato_regular);

        tvSignupFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignupActivity.this, "Pronto! Login con FB", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignupBiin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        tvOptionLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        /*  desarrollo y pruebas  */
        setUpEnvironment(lato_black);
    }


    /*  para efectos de desarrollo y pruebas  */

    private final String heroku = "Desarrollo (herokuapp.com) ";
    private final String biinio = "Produccion (biin.io) ";

    private Switch swEnvironment;

    private void setUpEnvironment(Typeface lato_black){
        swEnvironment = (Switch)findViewById(R.id.swEnvironment);
        swEnvironment.setTypeface(lato_black);
        swEnvironment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BNAppManager.getInstance().getNetworkManagerInstance().setProduction(isChecked);
                if(BNAppManager.getInstance().getNetworkManagerInstance().isProduction()){
                    swEnvironment.setText(biinio);
                }else{
                    swEnvironment.setText(heroku);
                }
            }
        });

        if(BNAppManager.getInstance().getNetworkManagerInstance().isProduction()){
            swEnvironment.setText(biinio);
            swEnvironment.setChecked(true);
        }else{
            swEnvironment.setText(heroku);
            swEnvironment.setChecked(false);
        }
    }

}
