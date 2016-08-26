package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setUpScreen();
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        TextView tvHeader = (TextView)findViewById(R.id.tvAboutHeader);
        tvHeader.setTypeface(lato_black);
        tvHeader.setLetterSpacing(0.3f);

        TextView tvAbout = (TextView)findViewById(R.id.tvAboutText);
        tvAbout.setTypeface(lato_regular);

        TextView tvVersion = (TextView)findViewById(R.id.tvAboutVersion);
        tvVersion.setTypeface(lato_regular);
        tvVersion.setText(tvVersion.getText().toString() + " " + BNUtils.getVersion());

        TextView tvWeb = (TextView)findViewById(R.id.tvAboutWeb);
        tvWeb.setTypeface(lato_regular);
        tvWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.biin.io";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        BNToolbar toolbar = new BNToolbar(this, getResources().getString(R.string.About));
    }
}
