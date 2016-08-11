package com.biin.biin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.biin.biin.Adapters.BNGiftAdapter;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Managers.BNAppManager;

import java.util.ArrayList;
import java.util.List;

public class GiftsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_list);

        RecyclerView rvSlides = (RecyclerView)findViewById(R.id.rvGiftsList);
        List<BNGift> gifts = new ArrayList<>(BNAppManager.getInstance().getDataManagerInstance().getBNGifts().values());
        if(gifts.size() > 0) {
            BNGiftAdapter adapter = new BNGiftAdapter(this, gifts);
            rvSlides.setLayoutManager(new LinearLayoutManager(this));
            rvSlides.setAdapter(adapter);
        }else{
            LinearLayout vlEmptyGifts = (LinearLayout)findViewById(R.id.vlEmptyGifts);
            vlEmptyGifts.setVisibility(View.VISIBLE);
        }
    }
}
