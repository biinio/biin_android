package com.biin.biin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.biin.biin.Adapters.BNGiftAdapter;

public class GiftsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_list);

        RecyclerView rvSlides = (RecyclerView)findViewById(R.id.rvGiftsList);
        BNGiftAdapter adapter = new BNGiftAdapter(this);
        rvSlides.setLayoutManager(new LinearLayoutManager(this));
        rvSlides.setAdapter(adapter);
    }
}
