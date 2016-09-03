package com.biin.biin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;

public class LoyaltyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_list);

        setUpScreen();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvLoyalties = (TextView) findViewById(R.id.tvEmptyLoyalties);
        tvLoyalties.setTypeface(lato_regular);
        tvLoyalties.setLetterSpacing(0.3f);

        BNToolbar toolbar = new BNToolbar(this, getResources().getString(R.string.LoyaltyWallet));
    }

}
