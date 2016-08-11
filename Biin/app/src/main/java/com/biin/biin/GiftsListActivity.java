package com.biin.biin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biin.biin.Adapters.BNGiftAdapter;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;

import java.util.ArrayList;
import java.util.List;

public class GiftsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_list);

        setUpScreen();

        RecyclerView rvSlides = (RecyclerView) findViewById(R.id.rvGiftsList);
        List<BNGift> gifts = new ArrayList<>(BNAppManager.getInstance().getDataManagerInstance().getBNGifts().values());
        if (gifts.size() > 0) {
            BNGiftAdapter adapter = new BNGiftAdapter(this, gifts);
            rvSlides.setLayoutManager(new LinearLayoutManager(this));
            rvSlides.setAdapter(adapter);
        } else {
            LinearLayout vlEmptyGifts = (LinearLayout) findViewById(R.id.vlEmptyGifts);
            vlEmptyGifts.setVisibility(View.VISIBLE);
        }
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvTitle = (TextView) findViewById(R.id.tvGiftsListTitle);
        tvTitle.setTypeface(lato_regular);
        tvTitle.setLetterSpacing(0.3f);

        TextView tvGifts = (TextView) findViewById(R.id.tvEmptyGifts);
        tvGifts.setTypeface(lato_regular);
        tvGifts.setLetterSpacing(0.3f);

        findViewById(R.id.ivGiftsListBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
