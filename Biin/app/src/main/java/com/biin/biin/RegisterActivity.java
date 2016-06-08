package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.biin.biin.Utils.BNUtils;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvGender, tvBirthdate, tvRegister;
    private FormEditText etName, etLastName, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpScreen();

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        findViewById(R.id.ivRegisterBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToSignUp();
            }
        });
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
    }

    private void signUp(){
        if(checkFields()){
            Intent i = new Intent(RegisterActivity.this, SplashActivity.class);
            startActivity(i);
            finish();
        }
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
    public void onBackPressed() {
        returnToSignUp();
    }

    private void returnToSignUp(){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }
}
