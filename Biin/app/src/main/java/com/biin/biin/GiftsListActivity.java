package com.biin.biin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biin.biin.Adapters.BNGiftAdapter;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNUtils;

import java.util.ArrayList;
import java.util.List;

public class GiftsListActivity extends AppCompatActivity {

    private static final String TAG = "GiftsListActivity";

    private BNDataManager dataManager;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private BNGiftAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_list);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String type = intent.getStringExtra("TYPE");
                if(type.equals("GIFT")) {
                    Log.e(TAG, "Gift recibido por notificacion");
                    if(adapter != null){
                        if(adapter.getItemCount() > 0){
                            adapter.notifyItemInserted(0);
                        }
                    }else{
                        setUpList();
                    }
                }
            }
        };

        setUpScreen();
        setUpList();
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

        dataManager.clearGiftsBadge();
    }

    private void setUpList(){
        RecyclerView rvSlides = (RecyclerView) findViewById(R.id.rvGiftsList);
        List<BNGift> gifts = new ArrayList<>(dataManager.getBNGifts().values());
        if (gifts.size() > 0) {
            adapter = new BNGiftAdapter(this, gifts);
            rvSlides.setLayoutManager(new LinearLayoutManager(this));
            rvSlides.setAdapter(adapter);
        } else {
            LinearLayout vlEmptyGifts = (LinearLayout) findViewById(R.id.vlEmptyGifts);
            vlEmptyGifts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("MESSAGING_SERVICE");
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

}
