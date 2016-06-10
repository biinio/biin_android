package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.biin.biin.Volley.Listeners.BNSignupListener;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, BNSignupListener.IBNSignupListener, BNBiiniesListener.IBNBiiniesListener {

    private static final String TAG = "RegisterActivity";

    private BNSignupListener signupListener;
    private BNBiiniesListener biiniesListener;

    private TextView tvGender, tvBirthdate, tvRegister;
    private FormEditText etName, etLastName, etEmail, etPassword;
    private ImageView ivMale, ivFemale;

    private String date, gender, male, female;
    private int colorNormal, colorSelected;

    private View.OnClickListener signupClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tvRegister.setOnClickListener(null);
            signUp();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpScreen();
    }

    private void setUpScreen() {
        Typeface lato_light = BNUtils.getLato_light();
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvRegisterTitle = (TextView) findViewById(R.id.tvRegisterTitle);
        TextView tvShareWithUs = (TextView) findViewById(R.id.tvShareWithUs);
        TextView tvCheckMail = (TextView) findViewById(R.id.tvCheckMail);

        tvGender = (TextView) findViewById(R.id.tvRegisterGender);
        tvBirthdate = (TextView) findViewById(R.id.tvRegisterBirthdate);
        tvRegister = (TextView) findViewById(R.id.tvRegisterFinish);
        etName = (FormEditText) findViewById(R.id.etRegisterName);
        etLastName = (FormEditText) findViewById(R.id.etRegisterLastName);
        etEmail = (FormEditText) findViewById(R.id.etRegisterEmail);
        etPassword = (FormEditText) findViewById(R.id.etRegisterPass);
        ivMale = (ImageView) findViewById(R.id.ivRegisterMale);
        ivFemale = (ImageView) findViewById(R.id.ivRegisterFemale);

        tvRegisterTitle.setTypeface(lato_regular);
        tvShareWithUs.setTypeface(lato_regular);
        tvCheckMail.setTypeface(lato_regular);

        tvRegister.setTypeface(lato_regular);
        etName.setTypeface(lato_light);
        etLastName.setTypeface(lato_light);
        etEmail.setTypeface(lato_light);
        etPassword.setTypeface(lato_light);
        tvGender.setTypeface(lato_light);
        tvBirthdate.setTypeface(lato_light);

        tvRegisterTitle.setLetterSpacing(0.3f);

        int etNamePaddingLeft = etName.getPaddingLeft();
        int etNamePaddingTop = etName.getPaddingTop();
        int etNamePaddingRight = etName.getPaddingRight();
        int etNamePaddingBottom = etName.getPaddingBottom();

        etName.setBackgroundResource(R.color.colorAccent);
        etName.setPadding(etNamePaddingLeft, etNamePaddingTop, etNamePaddingRight, etNamePaddingBottom);

        int etLastNamePaddingLeft = etLastName.getPaddingLeft();
        int etLastNamePaddingTop = etLastName.getPaddingTop();
        int etLastNamePaddingRight = etLastName.getPaddingRight();
        int etLastNamePaddingBottom = etLastName.getPaddingBottom();

        etLastName.setBackgroundResource(R.color.colorAccent);
        etLastName.setPadding(etLastNamePaddingLeft, etLastNamePaddingTop, etLastNamePaddingRight, etLastNamePaddingBottom);

        etEmail.setBackgroundResource(R.color.colorAccent);
        etEmail.setPadding(etLastNamePaddingLeft, etLastNamePaddingTop, etLastNamePaddingRight, etLastNamePaddingBottom);

        etPassword.setBackgroundResource(R.color.colorAccent);
        etPassword.setPadding(etLastNamePaddingLeft, etLastNamePaddingTop, etLastNamePaddingRight, etLastNamePaddingBottom);

        date = "none";
        gender = "none";
        male = getResources().getString(R.string.GenderMale);
        female = getResources().getString(R.string.GenderFemale);
        colorNormal = getResources().getColor(R.color.colorPrimary);
        colorSelected = getResources().getColor(R.color.colorOrange);

        ivMale.setOnClickListener(maleClick);
        ivFemale.setOnClickListener(femaleClick);
        tvBirthdate.setOnClickListener(dateClick);
        tvRegister.setOnClickListener(signupClick);

        findViewById(R.id.ivRegisterBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToSignUp();
            }
        });
    }

    private View.OnClickListener maleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tvGender.setText(male);
            gender = "male";
            ivMale.setImageDrawable(getDrawable(R.drawable.male_sel));
            ivMale.setColorFilter(colorSelected);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female));
            ivFemale.setColorFilter(colorNormal);
        }
    };

    private View.OnClickListener femaleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tvGender.setText(female);
            gender = "female";
            ivMale.setImageDrawable(getDrawable(R.drawable.male));
            ivMale.setColorFilter(colorNormal);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female_sel));
            ivFemale.setColorFilter(colorSelected);
        }
    };

    private View.OnClickListener dateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar calendar = Calendar.getInstance();
            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(RegisterActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

            datePickerDialog.show(getSupportFragmentManager(), TAG);
        }
    };

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat apiFormatter = new SimpleDateFormat(BNUtils.getActionDateFormat());
        date = apiFormatter.format(calendar.getTime());
        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
        tvBirthdate.setText(showFormatter.format(calendar.getTime()));
    }

    private void signUp(){
        if(checkFields()) {
            signupRequest();
        }else{
            ivMale.setOnClickListener(maleClick);
            ivFemale.setOnClickListener(femaleClick);
            tvBirthdate.setOnClickListener(dateClick);
            tvRegister.setOnClickListener(signupClick);
        }
    }

    private boolean checkFields(){
        boolean allValid = true;
        FormEditText[] allFields = { etName, etLastName, etEmail, etPassword };

        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        return allValid;
    }

    private void signupRequest(){
        signupListener = new BNSignupListener();
        signupListener.setListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getNetworkManagerInstance().getRegisterUrl(etName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), gender, date),
                null,
                signupListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "Signup");
    }

    @Override
    public void onSignupResponse(String identifier) {
        if(!identifier.isEmpty()){
            getBiinie(identifier);
        }else{
            Log.e(TAG, "Error: no se obtuvieron datos");
            Toast.makeText(this, getString(R.string.ServerErrorText), Toast.LENGTH_LONG).show();
            ivMale.setOnClickListener(maleClick);
            ivFemale.setOnClickListener(femaleClick);
            tvBirthdate.setOnClickListener(dateClick);
            tvRegister.setOnClickListener(signupClick);
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
        ivMale.setOnClickListener(maleClick);
        ivFemale.setOnClickListener(femaleClick);
        tvBirthdate.setOnClickListener(dateClick);
        tvRegister.setOnClickListener(signupClick);
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

        Intent i = new Intent(RegisterActivity.this, PrivacyActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBiinieError() {
        Log.e(TAG, getString(R.string.RequestFailed));
        ivMale.setOnClickListener(maleClick);
        ivFemale.setOnClickListener(femaleClick);
        tvBirthdate.setOnClickListener(dateClick);
        tvRegister.setOnClickListener(signupClick);
        Toast.makeText(this, getString(R.string.RequestFailed), Toast.LENGTH_SHORT).show();
    }
}
