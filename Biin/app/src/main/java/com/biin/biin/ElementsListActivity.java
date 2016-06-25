package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Adapters.BNElementListAdapter;
import com.biin.biin.Components.LinearLayoutManagerSmooth;
import com.biin.biin.Components.Listeners.BNLoadMoreElementsListener;
import com.biin.biin.Components.Listeners.BNLoadMoreSitesListener;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNElementsListener;
import com.biin.biin.Volley.Listeners.BNSitesListener;

import java.util.ArrayList;
import java.util.List;

public class ElementsListActivity extends AppCompatActivity implements BNElementsListener.IBNElementsListener {

    private static final String TAG = "ElementsListActivity";

    private BNCategory currentCategory;
    private String categoryIdentifier;

    private TextView tvTitle;
    private RecyclerView rvElementsList;

    private Biinie biinie;
    private BNElementsListener elementsListener;
    private BNElementListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements_list);

        setUpScreen();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        categoryIdentifier = preferences.getString(BNUtils.BNStringExtras.BNCategory, "favorites");

        if(categoryIdentifier.equals("favorites")){
            loadFavorites();
        }else {
            loadElements();
        }
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();

        tvTitle = (TextView)findViewById(R.id.tvElementsListTitle);
        tvTitle.setTypeface(lato_regular);
        tvTitle.setLetterSpacing(0.3f);

        findViewById(R.id.ivElementsListBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        biinie = BNAppManager.getDataManagerInstance().getBiinie();
    }

    private void loadElements() {
        currentCategory = BNAppManager.getDataManagerInstance().getBNCategory(categoryIdentifier);

        if (currentCategory != null) {
            tvTitle.setText(getResources().getIdentifier(currentCategory.getIdentifier(), "string", getPackageName()));

            rvElementsList = (RecyclerView)findViewById(R.id.rvElementsList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//            LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.VERTICAL, false);

            adapter = new BNElementListAdapter(this, currentCategory.getElements(), rvElementsList);
            adapter.setOnLoadMoreListener(new BNLoadMoreElementsListener(){
                @Override
                public void onLoadMoreElements() {
                    currentCategory.getElements().add(null);
                    adapter.notifyItemInserted(currentCategory.getElements().size() - 1);

                    elementsListener = new BNElementsListener(currentCategory.getElements(), false);
                    elementsListener.setListener(ElementsListActivity.this);

                    String url = BNAppManager.getNetworkManagerInstance().getMoreCategoryElementsUrl(biinie.getIdentifier(), categoryIdentifier);
                    Log.d(TAG, url);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            elementsListener,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    onLoadMoreError(error);
                                }
                            });
                    BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "MoreElementsData");
                }
            });
            rvElementsList.setLayoutManager(layoutManager);
            rvElementsList.setHasFixedSize(true);
            rvElementsList.setAdapter(adapter);
        }
    }

    private void loadFavorites() {
        List<BNElement> favorites = new ArrayList<>(BNAppManager.getDataManagerInstance().getFavouriteBNElements().values());

        rvElementsList = (RecyclerView) findViewById(R.id.rvElementsList);
        if(favorites.size() > 0) {
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.VERTICAL, false);

            BNElementListAdapter adapter = new BNElementListAdapter(this, favorites);
            rvElementsList.setLayoutManager(layoutManager);
            rvElementsList.setHasFixedSize(true);
            rvElementsList.setAdapter(adapter);
        }else{
            Typeface lato_regular = BNUtils.getLato_regular();

            LinearLayout vlFavourites = (LinearLayout) findViewById(R.id.vlAddFavouriteElements);
            rvElementsList.setVisibility(View.GONE);
            vlFavourites.setVisibility(View.VISIBLE);

            TextView tvFavorites = (TextView)findViewById(R.id.tvEmptyFavourites);
            tvFavorites.setTypeface(lato_regular);
            tvFavorites.setLetterSpacing(0.3f);
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
    public void onLoadMoreElementsResponse() {
        adapter.setLoaded();
    }

    private void onLoadMoreError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
        adapter.setLoaded();
    }
}
