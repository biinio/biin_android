package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.biin.biin.Adapters.BNSiteListAdapter;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;

import java.util.ArrayList;
import java.util.List;

public class SitesListActivity extends AppCompatActivity {

    private static final String TAG = "SitesListActivity";
    private boolean isFavourites = false;

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_list);

        Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
        tvTitle = (TextView)findViewById(R.id.tvSitesListTitle);
        tvTitle.setTypeface(lato_regular);
        tvTitle.setLetterSpacing(0.3f);

        Intent i = getIntent();
        if(i != null){
            isFavourites = i.getBooleanExtra(BNUtils.BNStringExtras.BNFavorites, false);
        }

        List<BNSite> sites;
        if(isFavourites) {
            sites = new ArrayList<>(BNAppManager.getDataManagerInstance().getFavouriteBNSites().values());
            tvTitle.setText(getResources().getString(R.string.FavoritePlaces));
        }else {
            sites = new ArrayList<>(BNAppManager.getDataManagerInstance().getNearByBNSites().values());
        }

        RecyclerView rvSites = (RecyclerView)findViewById(R.id.rvSitesList);
//        rvSites.setSnapEnabled(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        BNSiteListAdapter adapter = new BNSiteListAdapter(this, sites);
        adapter.setShowOthers(true);
        rvSites.setLayoutManager(layoutManager);
        rvSites.setHasFixedSize(true);
        rvSites.setAdapter(adapter);

        findViewById(R.id.ivSitesListBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
