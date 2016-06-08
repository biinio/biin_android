package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.biin.biin.Utils.BNUtils;

public class LoginActivity extends AppCompatActivity {

    private TextView tvLoginTitle, tvLoginHint, tvLoginBiin;
    private FormEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpScreen();

        tvLoginBiin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        findViewById(R.id.ivLoginBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToSignUp();
            }
        });
    }

    private void setUpScreen(){
        Typeface lato_light = BNUtils.getLato_light();
        Typeface lato_regular = BNUtils.getLato_regular();

        tvLoginTitle = (TextView)findViewById(R.id.tvLoginTitle);
        tvLoginHint = (TextView)findViewById(R.id.tvLoginHint);
        tvLoginBiin = (TextView)findViewById(R.id.tvLoginBiin);
        etEmail = (FormEditText)findViewById(R.id.etLoginEmail);
        etPassword = (FormEditText)findViewById(R.id.etLoginPass);

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
    }

    private void logIn(){
//        if(checkFields()){
            Intent i = new Intent(LoginActivity.this, PrivacyActivity.class);
            startActivity(i);
            finish();
//        }
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
