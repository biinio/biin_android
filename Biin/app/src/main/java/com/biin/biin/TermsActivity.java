package com.biin.biin;

import android.content.Intent;
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

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        setUpScreen();
        loadWebData();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        ImageView ivBack = (ImageView)findViewById(R.id.ivTermsBack);
        TextView tvTitle = (TextView)findViewById(R.id.tvTermsTitle);
        TextView tvContinue = (TextView)findViewById(R.id.tvTermsContinue);

        tvTitle.setTypeface(lato_regular);
        tvContinue.setTypeface(lato_black);

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TermsActivity.this, OnboardingActivity.class);
                startActivity(i);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToPrevious();
            }
        });
    }

    private void loadWebData() {
        String lang = BNUtils.getLanguaje();
        WebView wvTerms = (WebView)findViewById(R.id.wvTerms);
        wvTerms.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.tvTermsContinue).setVisibility(View.VISIBLE);
                findViewById(R.id.pbTermsLoading).setVisibility(View.GONE);
            }
        });
        wvTerms.loadUrl(BNAppManager.getNetworkManagerInstance().getTermsOfUse(lang));
    }

    @Override
    public void onBackPressed() {
        returnToPrevious();
    }

    private void returnToPrevious(){
        Intent i = new Intent(this, PrivacyActivity.class);
        startActivity(i);
        finish();
    }

}
