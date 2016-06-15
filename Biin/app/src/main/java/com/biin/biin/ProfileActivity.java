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
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNBiiniesListener;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, BNBiiniesListener.IBNBiiniesListener {

    private static final String TAG = "ProfileActivity";

    private BNBiiniesListener biiniesListener;

    private FormEditText etName, etLastName, etEmail;
    private TextView etUsername, etVerified, etBirthdate, tvGender, tvLoginFb, tvSave;
    private ImageView ivMale, ivFemale;

    private String date, gender, male, female, none;
    private int colorNormal, colorSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUpScreen();
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        ImageView ivBack = (ImageView)findViewById(R.id.ivProfileBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvTitle = (TextView)findViewById(R.id.tvProfileTitle);
        tvTitle.setTypeface(lato_regular);
        tvTitle.setLetterSpacing(0.3f);

        TextView tvName = (TextView) findViewById(R.id.tvProfileName);
        TextView tvLastname = (TextView) findViewById(R.id.tvProfileLastName);
        TextView tvUsername = (TextView) findViewById(R.id.tvProfileUserName);
        TextView tvEmail = (TextView) findViewById(R.id.tvProfileEmail);
        TextView tvVerified = (TextView) findViewById(R.id.tvProfileVerified);
        TextView tvBirthdate = (TextView) findViewById(R.id.tvProfileBirthDate);

        tvGender = (TextView) findViewById(R.id.tvProfileGender);

        etName = (FormEditText) findViewById(R.id.etProfileName);
        etLastName = (FormEditText) findViewById(R.id.etProfileLastName);
        etUsername = (TextView) findViewById(R.id.etProfileUserName);
        etEmail = (FormEditText) findViewById(R.id.etProfileEmail);
        etVerified = (TextView) findViewById(R.id.etProfileVerified);
        etBirthdate = (TextView) findViewById(R.id.etProfileBirthDate);

        tvLoginFb = (TextView)findViewById(R.id.tvProfileFb);
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

        setUpFields();
//        getBiinie(BNAppManager.getDataManagerInstance().getBiinie().getIdentifier());
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

    private void setUpFields(){
        Biinie biinie = BNAppManager.getDataManagerInstance().getBiinie();

        etName.setText(biinie.getFirstName());
        etLastName.setText(biinie.getLastName());
        etUsername.setText(biinie.getBiinName());
        etEmail.setText(biinie.getEmail());

        if(biinie.isEmailVerified()) {
            etVerified.setText(getString(R.string.Yes));
            etVerified.setTextColor(getResources().getColor(R.color.colorLightGrey));
        }else{
            etVerified.setText(getString(R.string.No));
        }

        SimpleDateFormat formatter = new SimpleDateFormat(BNUtils.getUserDateFormat());
        etBirthdate.setText(formatter.format(biinie.getBirthDate()));

        SimpleDateFormat apiFormatter = new SimpleDateFormat(BNUtils.getActionDateFormat());
        date = apiFormatter.format(biinie.getBirthDate());

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
            ivMale.setImageDrawable(getDrawable(R.drawable.male_sel));
            ivMale.setColorFilter(colorSelected);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female));
            ivFemale.setColorFilter(colorNormal);
        }else if(gender.equals("female")){
            tvGender.setText(female);
            this.gender = "female";
            ivMale.setImageDrawable(getDrawable(R.drawable.male));
            ivMale.setColorFilter(colorNormal);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female_sel));
            ivFemale.setColorFilter(colorSelected);
        }else{
            tvGender.setText(none);
            this.gender = "none";
            ivMale.setImageDrawable(getDrawable(R.drawable.male));
            ivMale.setColorFilter(colorNormal);
            ivFemale.setImageDrawable(getDrawable(R.drawable.female));
            ivFemale.setColorFilter(colorNormal);
        }
    }

    private View.OnClickListener dateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar calendar = Calendar.getInstance();
            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ProfileActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

            datePickerDialog.show(getSupportFragmentManager(), TAG);
        }
    };

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat apiFormatter = new SimpleDateFormat(BNUtils.getActionDateFormat());
        date = apiFormatter.format(calendar.getTime());
        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getUserDateFormat());
        etBirthdate.setText(showFormatter.format(calendar.getTime()));
    }

    @Override
    public void onBiiniesLoaded() {
        setUpFields();
        Log.e(TAG, "Biinie cargado correctamente");
    }

    @Override
    public void onBiinieError() {
        Log.e(TAG, getString(R.string.RequestFailed));
//        tvLoginBiin.setOnClickListener(loginClick);
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
//        tvLoginBiin.setOnClickListener(loginClick);
    }

}
