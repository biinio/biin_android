package com.biin.biin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biin.biin.Adapters.BNLoyaltyAdapter;
import com.biin.biin.Components.Listeners.IBNLoyaltyCardActionListener;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyListActivity extends AppCompatActivity implements IBNLoyaltyCardActionListener {

    private static final String TAG = "LoyaltyListActivity";

    private BNDataManager dataManager;
    private List<BNLoyalty> loyalties;
    private BNLoyaltyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_list);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();

        setUpScreen();
        setUpList();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvLoyalties = (TextView) findViewById(R.id.tvEmptyLoyalties);
        tvLoyalties.setTypeface(lato_regular);
        tvLoyalties.setLetterSpacing(0.3f);

        BNToolbar toolbar = new BNToolbar(this, getResources().getString(R.string.LoyaltyWallet));
    }

    private void setUpList(){
        RecyclerView rvSlides = (RecyclerView) findViewById(R.id.rvLoyaltiesList);
        LinearLayout vlEmptyLoyalties = (LinearLayout) findViewById(R.id.vlEmptyLoyalties);
        loyalties = new ArrayList<>(dataManager.getBNLoyalties());
        if (loyalties.size() > 0) {
            adapter = new BNLoyaltyAdapter(this, loyalties, this);
            rvSlides.setLayoutManager(new LinearLayoutManager(this));
            rvSlides.setAdapter(adapter);
            vlEmptyLoyalties.setVisibility(View.GONE);
        } else {
            vlEmptyLoyalties.setVisibility(View.VISIBLE);
        }
    }

}
