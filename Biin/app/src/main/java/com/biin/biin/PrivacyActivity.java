package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        setUpScreen();
        loadWebData();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        ImageView ivBack = (ImageView)findViewById(R.id.ivPrivacyBack);
        TextView tvTitle = (TextView)findViewById(R.id.tvPrivacyTitle);
        TextView tvContinue = (TextView)findViewById(R.id.tvPrivacyContinue);

        tvTitle.setTypeface(lato_regular);
        tvContinue.setTypeface(lato_black);

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrivacyActivity.this, TermsActivity.class);
                startActivity(i);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToSignUp();
            }
        });
    }

    private void loadWebData() {
        String lang = BNUtils.getLanguaje();
        WebView wvPrivacy = (WebView)findViewById(R.id.wvPrivacy);
        wvPrivacy.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.tvPrivacyContinue).setVisibility(View.VISIBLE);
                findViewById(R.id.pbPrivacyLoading).setVisibility(View.GONE);
            }
        });
        wvPrivacy.loadUrl(BNAppManager.getInstance().getNetworkManagerInstance().getPrivacyPolicies(lang));
    }

    @Override
    public void onBackPressed() {
        returnToSignUp();
    }

    private void returnToSignUp(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(BNUtils.BNStringExtras.BNBiinie);
        editor.commit();

        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }

}
