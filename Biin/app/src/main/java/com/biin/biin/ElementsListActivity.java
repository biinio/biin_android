package com.biin.biin;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.biin.biin.Adapters.BNElementListAdapter;
import com.biin.biin.Components.LinearLayoutManagerSmooth;
import com.biin.biin.Components.Listeners.IBNElementsLikeListener;
import com.biin.biin.Components.Listeners.IBNLoadMoreElementsListener;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNElementsListener;
import com.biin.biin.Volley.Listeners.BNLikesListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ElementsListActivity extends AppCompatActivity implements BNElementsListener.IBNElementsListener, IBNElementsLikeListener, BNLikesListener.IBNLikesListener {

    private static final String TAG = "ElementsListActivity";

    private BNCategory currentCategory;
    private String categoryIdentifier;

    private RecyclerView rvElementsList;
    private TextView tvTitle;

    private BNDataManager dataManager;
    private Biinie biinie;
    private BNElementsListener elementsListener;
    private BNElementListAdapter adapter;
    private BNLikesListener likesListener;

    private int favouriteElementsVersion;
    private long animDuration = 300;

    private BNToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements_list);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();

        setUpScreen();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        categoryIdentifier = preferences.getString(BNUtils.BNStringExtras.BNCategory, BNUtils.BNStringExtras.BNFavorites);

        if(categoryIdentifier.equals(BNUtils.BNStringExtras.BNFavorites)){
            loadFavorites();
        }else {
            loadElements();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(categoryIdentifier.equals(BNUtils.BNStringExtras.BNFavorites)) {
            int favsVersion = dataManager.getFavouriteElementsVersion();

            if (favsVersion > favouriteElementsVersion) {
                favouriteElementsVersion = favsVersion;
                loadFavorites();
            }
        }
    }

    private void setUpScreen(){
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvFavorites = (TextView)findViewById(R.id.tvEmptyFavourites);
        tvFavorites.setTypeface(lato_regular);
        tvFavorites.setLetterSpacing(0.3f);

        biinie = dataManager.getBiinie();
        favouriteElementsVersion = dataManager.getFavouriteElementsVersion();

        likesListener = new BNLikesListener();
        likesListener.setListener(this);

        toolbar = new BNToolbar(this, getResources().getString(R.string.Collections));
    }

    private void loadElements() {
        currentCategory = dataManager.getBNCategory(categoryIdentifier);

        if (currentCategory != null) {
            toolbar.setTitle(getResources().getString(getResources().getIdentifier(currentCategory.getIdentifier(), "string", getPackageName())));

            rvElementsList = (RecyclerView)findViewById(R.id.rvElementsList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//            LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.VERTICAL, false);

            adapter = new BNElementListAdapter(this, currentCategory.getElements(), rvElementsList, this);
            adapter.setOnLoadMoreListener(new IBNLoadMoreElementsListener(){
                @Override
                public void onLoadMoreElements() {
                    currentCategory.getElements().add(null);
                    adapter.notifyItemInserted(currentCategory.getElements().size() - 1);

                    elementsListener = new BNElementsListener(currentCategory.getElements(), false);
                    elementsListener.setListener(ElementsListActivity.this);

                    String url = BNAppManager.getInstance().getNetworkManagerInstance().getMoreCategoryElementsUrl(biinie.getIdentifier(), categoryIdentifier);
                    Log.d(TAG, url);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            "",
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
        rvElementsList = (RecyclerView) findViewById(R.id.rvElementsList);
        if(dataManager.getFavouriteBNElements().size() > 0) {
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            LinearLayoutManagerSmooth layoutManager = new LinearLayoutManagerSmooth(this, LinearLayoutManager.VERTICAL, false);

            adapter = new BNElementListAdapter(this, dataManager.getFavouriteBNElements(), this);
            rvElementsList.setLayoutManager(layoutManager);
            rvElementsList.setHasFixedSize(true);
            rvElementsList.setAdapter(adapter);
        }else{
            LinearLayout vlFavourites = (LinearLayout) findViewById(R.id.vlAddFavouriteElements);
            rvElementsList.setVisibility(View.GONE);
            vlFavourites.setVisibility(View.VISIBLE);
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

    @Override
    public void onElementLiked(String identifier, int position) {
        likeElement(identifier, true);
    }

    @Override
    public void onElementUnliked(String identifier, final int position) {
        likeElement(identifier, false);

        if(categoryIdentifier.equals(BNUtils.BNStringExtras.BNFavorites)){
            adapter.notifyItemRemoved(position);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyItemRangeChanged(position, dataManager.getFavouriteBNElements().size());

                    if(!(dataManager.getFavouriteBNElements().size() > 0)) {
                        LinearLayout vlFavourites = (LinearLayout) findViewById(R.id.vlAddFavouriteElements);
                        rvElementsList.setVisibility(View.GONE);
                        vlFavourites.setVisibility(View.VISIBLE);
                    }
                }
            }, animDuration + 300);
        }
    }

    private void likeElement(final String identifier, final boolean liked){
        BNElement element = dataManager.getBNElement(identifier);
        element.setUserLiked(liked);

        if(liked) {
            dataManager.addFavouriteBNElement(element, 0);
        }else{
            dataManager.removeFavouriteBNElement(identifier);
        }

        String url = BNAppManager.getInstance().getNetworkManagerInstance().getLikeUrl(biinie.getIdentifier(), liked);
        Log.d(TAG, url);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = element.getModel();
            request.put("model", model);
        }catch (JSONException e){
            Log.e(TAG, "Error:" + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                request,
                likesListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLikeError(error, identifier, liked);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "LikeElement");
    }

    private void onLikeError(VolleyError error, String identifier, boolean liked){
        Log.e(TAG, "Error:" + error.getMessage());
        if(liked) {
            dataManager.addPendingLikeElement(identifier);
        }else{
            dataManager.addPendingUnlikeElement(identifier);
        }
    }

    @Override
    public void onLikeResponseOk() {

    }

    @Override
    public void onLikeResponseError() {

    }
}
