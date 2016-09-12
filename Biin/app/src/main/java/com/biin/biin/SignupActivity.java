package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNBiiniesListener;
import com.biin.biin.Volley.Listeners.BNSignupListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SignupActivity extends AppCompatActivity implements BNBiiniesListener.IBNBiiniesListener {

    private static final String TAG = "SignupActivity";

    private BNBiiniesListener signupListener;
    private BNBiiniesListener biiniesListener;
    private CallbackManager callbackManager;

    private TextView tvOptionLogin, tvSignupBiin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyBiinie();
    }

    private void verifyBiinie(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        String identifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");
        String environment = preferences.getString(BNUtils.BNStringExtras.Environment, "Dev");

        BNAppManager.getInstance().getNetworkManagerInstance().setProduction(environment.equals("Prod"));

        if(identifier.isEmpty()){
            LoginManager.getInstance().logOut();
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
                "",
                biiniesListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                        LoginManager.getInstance().logOut();
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "Biinie");
    }

    @Override
    public void onBiiniesLoaded() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BNUtils.BNStringExtras.BNBiinie, BNAppManager.getInstance().getDataManagerInstance().getBiinie().getIdentifier());
        editor.commit();

        Log.e(TAG, "Biinie cargado correctamente");

        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBiinieError() {
        Log.e(TAG, "Error cargando biinie");
        LoginManager.getInstance().logOut();
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

//        tvSignupFb = (TextView)findViewById(R.id.tvSignupFb);
        tvSignupBiin = (TextView)findViewById(R.id.tvSignupBiin);
        tvOptionLogin = (TextView)findViewById(R.id.tvOptionLogin);

        tvWelcome1.setTypeface(lato_regular);
        tvWelcome2.setTypeface(lato_regular);
//        tvSignupFb.setTypeface(lato_black);
        tvSignupBiin.setTypeface(lato_black);
        tvOptionLogin.setTypeface(lato_regular);

//        tvSignupFb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SignupActivity.this, "Pronto! Login con FB", Toast.LENGTH_SHORT).show();
//            }
//        });

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

        callbackManager = CallbackManager.Factory.create();

        LoginButton btnFacebook = (LoginButton) findViewById(R.id.btnLoginFb);
        btnFacebook.setTypeface(lato_black);
        btnFacebook.setReadPermissions(Arrays.asList("user_birthday", "public_profile", "email"));
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(SignupActivity.this, "Login ok", Toast.LENGTH_SHORT).show();
                graphRequest(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignupActivity.this, "Login cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignupActivity.this, "Login error", Toast.LENGTH_SHORT).show();
            }
        });

        /*  desarrollo y pruebas  */
        setUpEnvironment(lato_black);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void graphRequest(final LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG, response.toString());
                        try {
                            String id = "", name = "", gender = "", birthday = "", email = "";
                            if (object.has("id")) {
                                id = object.getString("id");
                            }
                            if (object.has("name")) {
                                name = object.getString("name");
                            }
                            if (object.has("gender")) {
                                gender = object.getString("gender");
                            }
                            if (object.has("birthday")) {
                                birthday = object.getString("birthday");
                            }
                            if (object.has("email")) {
                                email = object.getString("email");
                            }
                            signupRequest(name, email, "\"\"", gender, birthday, id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void signupRequest(String name, String email, String pass, String gender, String date, String facebookId){
        Date birthdate = BNUtils.getDateFromString(date, BNUtils.getFacebookDateFormat());
        String firstName, lastName;
        if(name.contains(" ")){
            firstName = name.substring(0, name.indexOf(" "));
            lastName = name.substring(name.indexOf(" ") + 1);
        }else{
            firstName = name;
            lastName = "";
        }

        signupListener = new BNBiiniesListener();
        signupListener.setListener(this);

        Biinie biinie = new Biinie();
        biinie.setFirstName(firstName);
        biinie.setLastName(lastName);
        biinie.setEmail(email);
        biinie.setGender(gender);
        biinie.setBirthDate(birthdate);
        biinie.setFacebook_id(facebookId);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = biinie.getModel();
            request.put("model", model);
        }catch (JSONException e){
            Log.e(TAG, "Error:" + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BNAppManager.getInstance().getNetworkManagerInstance().getUrlBiinie("none"),
                request,
                signupListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "SignupFb");
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
                SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if(BNAppManager.getInstance().getNetworkManagerInstance().isProduction()){
                    swEnvironment.setText(biinio);
                    editor.putString(BNUtils.BNStringExtras.Environment, "Prod");
                }else{
                    swEnvironment.setText(heroku);
                    editor.putString(BNUtils.BNStringExtras.Environment, "Dev");
                }
                editor.commit();
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
