package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.Entities.BNLoyaltyCard;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.google.android.gms.ads.internal.overlay.zzo;
import com.google.android.gms.vision.text.Line;

import java.text.SimpleDateFormat;
import java.util.List;

public class LoyaltyActivity extends AppCompatActivity {

    private static final String TAG = "LoyaltyActivity";

    private String cardIdentifier;
    private int position;

    private BNLoyaltyCard card;
    private BNOrganization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);

        Intent data = getIntent();
        cardIdentifier = data.getStringExtra(BNUtils.BNStringExtras.BNLoyalty);
        position = data.getIntExtra(BNUtils.BNStringExtras.Position, 0);

        BNDataManager dataManager = BNAppManager.getInstance().getDataManagerInstance();
        BNLoyalty loyalty = dataManager.getBNLoyalties().get(position);
        card = loyalty.getLoyaltyCard();
        organization = dataManager.getBNOrganization(loyalty.getOrganizationIdentifier());

        setUpScreen();
        setUpStars();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        ImageView ivCard = (ImageView) findViewById(R.id.ivLoyaltyCardImage);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(organization.getMedia().get(0).getUrl(), ivCard);

        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
        TextView tvDate = (TextView) findViewById(R.id.tvLoyaltyCardDate);
        tvDate.setTypeface(lato_regular);
        tvDate.setText(showFormatter.format(card.getStartDate()));

        TextView tvTitle = (TextView) findViewById(R.id.tvLoyaltyCardName);
        tvTitle.setTypeface(lato_black);
        tvTitle.setText(card.getTitle());

        TextView tvDetails = (TextView) findViewById(R.id.tvLoyaltyCardDetails);
        tvDetails.setTypeface(lato_regular);
        tvDetails.setText(card.getRule());

        TextView tvGoal = (TextView) findViewById(R.id.tvLoyaltyCardGoal);
        tvGoal.setTypeface(lato_regular);
        tvGoal.setText(card.getGoal());

        TextView tvGift = (TextView) findViewById(R.id.tvLoyaltyCardGift);
        tvGift.setTypeface(lato_black);
        tvGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoyaltyActivity.this, "Elements activity", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvToS = (TextView) findViewById(R.id.tvLoyaltyCardConditions);
        tvToS.setTypeface(lato_regular);
        tvToS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoyaltyActivity.this, "ToS, show popup", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView ivQrCode = (ImageView) findViewById(R.id.ivLoyaltyCardQrCode);

        findViewById(R.id.vlLoyaltyQrCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoyaltyActivity.this, "Read QR Code, show popup", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvCode = (TextView) findViewById(R.id.tvLoyaltyCardQrCode);
        tvCode.setTypeface(lato_black);

        if(BNUtils.calculateContrast(getResources().getColor(R.color.colorWhite), organization.getPrimaryColor(), organization.getSecondaryColor())) {
            tvCode.setTextColor(organization.getPrimaryColor());
            ivQrCode.setColorFilter(organization.getPrimaryColor());
            tvGift.setTextColor(organization.getPrimaryColor());
            tvTitle.setTextColor(organization.getPrimaryColor());
        }else{
            tvCode.setTextColor(organization.getSecondaryColor());
            ivQrCode.setColorFilter(organization.getSecondaryColor());
            tvGift.setTextColor(organization.getSecondaryColor());
            tvTitle.setTextColor(organization.getSecondaryColor());
        }


        BNToolbar toolbar = new BNToolbar(this, organization.getBrand());
    }

    private void setUpStars() {
        LinearLayout hlLoyaltyLineOpt4 = (LinearLayout)findViewById(R.id.hlLoyaltyLineOpt4);
        LinearLayout hlLoyaltyLineOpt2 = (LinearLayout)findViewById(R.id.hlLoyaltyLineOpt2);

        List<BNLoyaltyCard.BNLoyaltyCard_Slot> slots = card.getSlots();
        int size = slots.size();
        switch (size) {
            case 10:
                hlLoyaltyLineOpt2.setVisibility(View.VISIBLE);
                break;
            case 12:
                hlLoyaltyLineOpt4.setVisibility(View.VISIBLE);
                break;
            case 14:
                hlLoyaltyLineOpt2.setVisibility(View.VISIBLE);
                hlLoyaltyLineOpt4.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        boolean filled = true;
        for (int i = 0; i < size && filled; i++) {
            if(slots.get(i).isFilled()){
                switch (i + 1){
                    case 1:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar1)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 2:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar2)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 3:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar3)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 4:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar4)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 5:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar5)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 6:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar6)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 7:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar7)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 8:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar8)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 9:
                        if(size == 10) {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar13)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }else {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar9)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }
                        break;
                    case 10:
                        if(size == 10) {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar14)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }else {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar10)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }
                        break;
                    case 11:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar11)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 12:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar12)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 13:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar13)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 14:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar14)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    default:
                        break;
                }
            }else{
                filled = false;
            }
        }
    }
}
