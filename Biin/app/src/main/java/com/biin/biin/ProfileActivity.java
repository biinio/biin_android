package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNBiiniesListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, BNBiiniesListener.IBNBiiniesListener {

    private static final String TAG = "ProfileActivity";

    private BNBiiniesListener profileListener;
    private CallbackManager callbackManager;

    private LoginButton btnFacebook;
    private FormEditText etName, etLastName;
    private TextView etUsername, etVerified, etBirthdate, tvGender, etEmail, tvSave;
    private ImageView ivMale, ivFemale;

    private Date date;
    private String gender, male, female, none;
    private int colorNormal, colorSelected;
    private ProgressBar pbSave;

    private View.OnClickListener saveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tvSave.setOnClickListener(null);
            pbSave.setVisibility(View.VISIBLE);
            saveBiinie();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        callbackManager = CallbackManager.Factory.create();

        setUpScreen();
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        TextView tvName = (TextView) findViewById(R.id.tvProfileName);
        TextView tvLastname = (TextView) findViewById(R.id.tvProfileLastName);
        TextView tvUsername = (TextView) findViewById(R.id.tvProfileUserName);
        TextView tvEmail = (TextView) findViewById(R.id.tvProfileEmail);
        TextView tvVerified = (TextView) findViewById(R.id.tvProfileVerified);
        TextView tvBirthdate = (TextView) findViewById(R.id.tvProfileBirthDate);
        pbSave = (ProgressBar)findViewById(R.id.pbSave);

        tvGender = (TextView) findViewById(R.id.tvProfileGender);

        etName = (FormEditText) findViewById(R.id.etProfileName);
        etLastName = (FormEditText) findViewById(R.id.etProfileLastName);
        etUsername = (TextView) findViewById(R.id.etProfileUserName);
        etEmail = (TextView) findViewById(R.id.etProfileEmail);
        etVerified = (TextView) findViewById(R.id.etProfileVerified);
        etBirthdate = (TextView) findViewById(R.id.etProfileBirthDate);

        btnFacebook = (LoginButton) findViewById(R.id.btnProfileFb);
        tvSave = (TextView)findViewById(R.id.tvProfileSave);

        tvName.setTypeface(lato_black);
        tvLastname.setTypeface(lato_black);
        tvUsername.setTypeface(lato_black);
        tvEmail.setTypeface(lato_black);
        tvVerified.setTypeface(lato_black);
        tvBirthdate.setTypeface(lato_black);

        tvGender.setTypeface(lato_black);

        etName.setTypeface(lato_regular);
        etLastName.setTypeface(lato_regular);
        etUsername.setTypeface(lato_regular);
        etEmail.setTypeface(lato_regular);
        etVerified.setTypeface(lato_regular);
        etBirthdate.setTypeface(lato_regular);

        btnFacebook.setTypeface(lato_black);
        tvSave.setTypeface(lato_black);

        btnFacebook.setReadPermissions(Arrays.asList("user_birthday", "public_profile", "email"));
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(ProfileActivity.this, R.string.login_facebook_ok, Toast.LENGTH_SHORT).show();
                graphRequest(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(ProfileActivity.this, R.string.login_facebook_canceled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ProfileActivity.this, R.string.login_facebook_error, Toast.LENGTH_SHORT).show();
            }
        });

        ivMale = (ImageView) findViewById(R.id.ivProfileMale);
        ivFemale = (ImageView) findViewById(R.id.ivProfileFemale);

        ivMale.setOnClickListener(maleClick);
        ivFemale.setOnClickListener(femaleClick);
        etBirthdate.setOnClickListener(dateClick);

        male = getResources().getString(R.string.GenderMale);
        female = getResources().getString(R.string.GenderFemale);
        none = getResources().getString(R.string.Gender);

        colorNormal = getResources().getColor(R.color.colorPrimary);
        colorSelected = getResources().getColor(R.color.colorOrange);

        BNToolbar toolbar = new BNToolbar(this, getResources().getString(R.string.Profile));

        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();
        setUpFields(biinie);

        tvSave.setOnClickListener(saveClick);
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
                            setUpFields(setUpBiinie(name, email, gender, birthday, id));
                            saveRequest();
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

    private Biinie setUpBiinie(String name, String email, String gender, String birthDate, String facebookId){
        String firstName, lastName;
        if(name.contains(" ")){
            firstName = name.substring(0, name.indexOf(" "));
            lastName = name.substring(name.indexOf(" ") + 1);
        }else{
            firstName = name;
            lastName = "";
        }

        Biinie biinie = new Biinie();
        biinie.setFirstName(firstName);
        biinie.setLastName(lastName);
        biinie.setBiinName(email);
        biinie.setEmail(email);
        biinie.setEmailVerified(true);
        biinie.setGender(gender);
        biinie.setBirthDate(BNUtils.getDateFromString(birthDate, BNUtils.getFacebookDateFormat()));
        biinie.setFacebook_id(facebookId);

        return biinie;
    }

    private void setUpFields(Biinie biinie){
        etName.setText(biinie.getFirstName());
        etLastName.setText(biinie.getLastName());
        etUsername.setText(biinie.getBiinName());
        etEmail.setText(biinie.getEmail());

        if(biinie.isEmailVerified()) {
            etVerified.setText(getString(R.string.Yes));
            etVerified.setTextColor(getResources().getColor(R.color.colorAccentGray));
        }else{
            etVerified.setText(getString(R.string.No));
        }

        SimpleDateFormat formatter = new SimpleDateFormat(BNUtils.getUserDateFormat());
        etBirthdate.setText(formatter.format(biinie.getBirthDate()));

        date = biinie.getBirthDate();

        gender = biinie.getGender();
        setGender(gender);
    }

    private View.OnClickListener maleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setGender("male");
        }
    };

    private View.OnClickListener femaleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setGender("female");
        }
    };

    private void setGender(String gender){
        if(gender.equals("male")){
            tvGender.setText(male);
            this.gender = "male";
            ivMale.setImageDrawable(getDrawable(R.drawable.male_filled));
            ivMale.setColorFilter(colorSelected);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female_empty));
            ivFemale.setColorFilter(colorNormal);
        }else if(gender.equals("female")){
            tvGender.setText(female);
            this.gender = "female";
            ivMale.setImageDrawable(getDrawable(R.drawable.male_empty));
            ivMale.setColorFilter(colorNormal);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female_filled));
            ivFemale.setColorFilter(colorSelected);
        }else{
            tvGender.setText(none);
            this.gender = "none";
            ivMale.setImageDrawable(getDrawable(R.drawable.male_empty));
            ivMale.setColorFilter(colorNormal);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female_empty));
            ivFemale.setColorFilter(colorNormal);
        }
    }

    private View.OnClickListener dateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ProfileActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

            datePickerDialog.show(getSupportFragmentManager(), TAG);
        }
    };

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        date = calendar.getTime();
        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getUserDateFormat());
        etBirthdate.setText(showFormatter.format(calendar.getTime()));
    }

    private void saveBiinie(){
        if(checkFields()){
            saveRequest();
        }else{
            tvSave.setOnClickListener(saveClick);
            pbSave.setVisibility(View.GONE);
        }
    }

    private boolean checkFields(){
        boolean allValid = true;
        FormEditText[] allFields = { etName, etLastName };

        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        return allValid;
    }

    private void saveRequest(){
        profileListener = new BNBiiniesListener();
        profileListener.setListener(this);

        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();
        biinie.setFirstName(etName.getText().toString().trim());
        biinie.setLastName(etLastName.getText().toString().trim());
        biinie.setGender(gender);
        biinie.setBirthDate(date);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = biinie.getModel();
            request.put("model", model);
        }catch (JSONException e){
            Log.e(TAG, "Error:" + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BNAppManager.getInstance().getNetworkManagerInstance().getUrlBiinie(biinie.getIdentifier()),
                request,
                profileListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "SaveProfile");
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
        tvSave.setOnClickListener(saveClick);
        pbSave.setVisibility(View.GONE);
    }

    @Override
    public void onBiiniesLoaded() {
        Log.e(TAG, "Biinie cargado correctamente");
        tvSave.setOnClickListener(saveClick);
        pbSave.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.SaveSuccess), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBiinieError() {
        Log.e(TAG, getString(R.string.RequestFailed));
        tvSave.setOnClickListener(saveClick);
        pbSave.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.RequestFailed), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
