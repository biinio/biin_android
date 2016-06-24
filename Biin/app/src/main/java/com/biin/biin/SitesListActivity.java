package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Adapters.BNSiteListAdapter;
import com.biin.biin.Components.Listeners.BNLoadMoreListener;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNSitesListener;

import java.util.ArrayList;
import java.util.List;

public class SitesListActivity extends AppCompatActivity implements BNSitesListener.IBNSitesListener {

    private static final String TAG = "SitesListActivity";

    private boolean isFavourites = false;
    private TextView tvTitle;
    private RecyclerView rvSites;

    private Biinie biinie;
    private BNSitesListener sitesListener;
    private BNSiteListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_list);

        setUpScreen();

        Intent i = getIntent();
        if(i != null){
            isFavourites = i.getBooleanExtra(BNUtils.BNStringExtras.BNFavorites, false);
        }

        final List<BNSite> sites;
        if(isFavourites) {
            sites = new ArrayList<>(BNAppManager.getDataManagerInstance().getFavouriteBNSites().values());
            tvTitle.setText(getResources().getString(R.string.FavoritePlaces));
        }else {
            sites = new ArrayList<>(BNAppManager.getDataManagerInstance().getNearByBNSites(true).values());
        }

        rvSites = (RecyclerView)findViewById(R.id.rvSitesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new BNSiteListAdapter(this, sites, rvSites, isFavourites);
        adapter.setShowOthers(true);
        adapter.setOnLoadMoreListener(new BNLoadMoreListener(){
            @Override
            public void onLoadMore(boolean isFavourites) {
                sites.add(null);
                adapter.notifyItemInserted(sites.size() - 1);

                sitesListener = new BNSitesListener(sites, isFavourites);
                sitesListener.setListener(SitesListActivity.this);

                String url = BNAppManager.getNetworkManagerInstance().getMoreSitesUrl(biinie.getIdentifier());
                Log.d(TAG, url);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        sitesListener,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                onLoadMoreError(error);
                            }
                        });
                BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "InitialData");
            }
        });
        rvSites.setLayoutManager(layoutManager);
        rvSites.setHasFixedSize(true);
        rvSites.setAdapter(adapter);

    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();
        tvTitle = (TextView)findViewById(R.id.tvSitesListTitle);
        tvTitle.setTypeface(lato_regular);
        tvTitle.setLetterSpacing(0.3f);

        findViewById(R.id.ivSitesListBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        biinie = BNAppManager.getDataManagerInstance().getBiinie();
    }

    @Override
    public void onItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void onLoadMoreSitesResponse() {
        adapter.setLoaded();
    }

    private void onLoadMoreError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
        adapter.setLoaded();
    }

}
