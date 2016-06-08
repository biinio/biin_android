package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.biin.biin.Utils.BNUtils;

public class SignupActivity extends AppCompatActivity {

    private TextView tvOptionLogin, tvSignupFb, tvSignupBiin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setUpScreen();

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
    }

    private void setUpScreen(){
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
    }

}
