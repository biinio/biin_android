package com.biin.biin;

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
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNBiiniesListener;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, BNBiiniesListener.IBNBiiniesListener {

    private static final String TAG = "ProfileActivity";

    private BNBiiniesListener profileListener;

    private FormEditText etName, etLastName;
    private TextView etUsername, etVerified, etBirthdate, tvGender, etEmail, tvLoginFb, tvSave;
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
        pbSave = (ProgressBar)findViewById(R.id.pbSave);

        tvGender = (TextView) findViewById(R.id.tvProfileGender);

        etName = (FormEditText) findViewById(R.id.etProfileName);
        etLastName = (FormEditText) findViewById(R.id.etProfileLastName);
        etUsername = (TextView) findViewById(R.id.etProfileUserName);
        etEmail = (TextView) findViewById(R.id.etProfileEmail);
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

        tvSave.setOnClickListener(saveClick);

        tvLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Pronto! Login con FB", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpFields(){
        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();

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
        biinie.setLastName(etLastName.getText().toString().trim());
        biinie.setFirstName(etName.getText().toString().trim());
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
}
