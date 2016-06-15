package com.biin.biin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.biin.biin.Adapters.BNElementListAdapter;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;

public class FavoritesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        setUpScreen();
        loadElements();
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();

//        tvTitle = (TextView)findViewById(R.id.tvElementsListTitle);
//        tvTitle.setTypeface(lato_regular);
//        tvTitle.setLetterSpacing(0.3f);

        findViewById(R.id.ivElementsListBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadElements() {
//        currentCategory = BNAppManager.getDataManagerInstance().getBNCategory(categoryIdentifier);
//
//        if (currentCategory != null) {
//            tvTitle.setText(getResources().getIdentifier(currentCategory.getIdentifier(),"string",getPackageName()));
//
//            RecyclerView rvElementsList = (RecyclerView)findViewById(R.id.rvElementsList);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//            BNElementListAdapter adapter = new BNElementListAdapter(this, currentCategory.getElements());
//            rvElementsList.setLayoutManager(layoutManager);
//            rvElementsList.setHasFixedSize(true);
//            rvElementsList.setAdapter(adapter);
//        }
    }
}
