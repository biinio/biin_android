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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Adapters.BNSiteListAdapter;
import com.biin.biin.Components.Listeners.IBNLoadMoreSitesListener;
import com.biin.biin.Components.Listeners.IBNSitesLikeListener;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNLikesListener;
import com.biin.biin.Volley.Listeners.BNSitesListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class SitesListActivity extends AppCompatActivity implements BNSitesListener.IBNSitesListener, IBNSitesLikeListener {

    private static final String TAG = "SitesListActivity";

    private boolean isFavourites = false;
    private RecyclerView rvSites;

    private BNDataManager dataManager;
    private Biinie biinie;
    private BNSitesListener sitesListener;
    private BNSiteListAdapter adapter;

    private int sitesVersion;
    private long animDuration = 300;

    private BNToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_list);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();

        Intent i = getIntent();
        if (i != null) {
            isFavourites = i.getBooleanExtra(BNUtils.BNStringExtras.BNFavorites, false);
        }

        setUpScreen();
        loadData();
    }

    private void setUpScreen() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvSites = (RecyclerView) findViewById(R.id.rvSitesList);
        rvSites.setLayoutManager(layoutManager);
        rvSites.setHasFixedSize(true);

        biinie = dataManager.getBiinie();
        if (isFavourites) {
            sitesVersion = dataManager.getFavouriteSitesVersion();
        } else {
            sitesVersion = dataManager.getNearBySitesVersion();
        }

        toolbar = new BNToolbar(this, getResources().getString(R.string.NearYou));
    }

    private void loadData(){
        final List<BNSite> sites;
        if (isFavourites) {
            sites = dataManager.getFavouriteBNSites();
            toolbar.setTitle(getResources().getString(R.string.FavoritePlaces));
        } else {
//          //TODO true para incluir favorites, false para omitirlos
            sites = dataManager.getNearByBNSites();
        }

        adapter = new BNSiteListAdapter(this, sites, this, rvSites, isFavourites);
        adapter.setShowOthers(true);
        adapter.setOnLoadMoreListener(new IBNLoadMoreSitesListener() {
            @Override
            public void onLoadMoreSites(boolean isFavourites) {
                sites.add(null);
                adapter.notifyItemInserted(sites.size() - 1);

                sitesListener = new BNSitesListener(sites, isFavourites);
                sitesListener.setListener(SitesListActivity.this);

                String url = BNAppManager.getInstance().getNetworkManagerInstance().getMoreSitesUrl(biinie.getIdentifier());
                Log.d(TAG, url);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        "",
                        sitesListener,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                onLoadMoreError(error);
                            }
                        });
                BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "MoreSitesData");
            }
        });
        rvSites.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFavourites) {
            int favsVersion = dataManager.getFavouriteSitesVersion();
            if (favsVersion > sitesVersion) {
                sitesVersion = favsVersion;
                loadData();
            }
        }else {
            int nearVersion = dataManager.getNearBySitesVersion();
            if (nearVersion > sitesVersion) {
                sitesVersion = nearVersion;
                loadData();
            }
        }
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

    private void onLoadMoreError(VolleyError error) {
        Log.e(TAG, "Error:" + error.getMessage());
        adapter.setLoaded();
    }

    @Override
    public void onSiteLiked(String identifier, int position) {
        if(dataManager.likeBNSite(identifier)){
            String ok = "ok";
        }
    }

    @Override
    public void onSiteUnliked(String identifier, final int position) {
        if(dataManager.unlikeBNSite(identifier)) {
            if (isFavourites) {
                adapter.notifyItemRemoved(position);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeChanged(position, dataManager.getFavouriteBNSites().size());

                        if (!(dataManager.getFavouriteBNSites().size() > 0)) {
                            LinearLayout vlFavourites = (LinearLayout) findViewById(R.id.vlAddFavouriteSites);
                            rvSites.setVisibility(View.GONE);
                            vlFavourites.setVisibility(View.VISIBLE);
                        }
                    }
                }, animDuration + 300);
            }
        }
    }

}
