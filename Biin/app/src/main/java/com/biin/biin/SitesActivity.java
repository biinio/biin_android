package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Components.Listeners.IBNLoadMoreSitesListener;
import com.biin.biin.Components.Listeners.IBNSitesLikeListener;
import com.biin.biin.Components.Listeners.IBNToolbarListener;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Utils.BNUtils.BNStringExtras;
import com.biin.biin.Adapters.BNShowcaseAdapter;
import com.biin.biin.Adapters.BNSiteAdapter;
import com.biin.biin.Components.CardRecyclerView;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Volley.Listeners.BNLikesListener;
import com.biin.biin.Volley.Listeners.BNSitesListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SitesActivity extends AppCompatActivity implements IBNSitesLikeListener, BNSitesListener.IBNSitesListener, IBNToolbarListener, BNLikesListener.IBNLikesListener {

    private static final String TAG = "SitesActivity";

    private BNSite currentSite;
    private String siteIdentifier;
    private ImageLoader imageLoader;
    private boolean showOthers = false;

    private Biinie biinie;
    private BNDataManager dataManager;
    private BNSitesListener sitesListener;
    private BNLikesListener likesListener;
    private BNSiteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);

        dataManager = BNAppManager.getDataManagerInstance();
        biinie = dataManager.getBiinie();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        siteIdentifier = preferences.getString(BNStringExtras.BNSite, "site");

        Intent i = getIntent();
        if(i != null){
            showOthers = i.getBooleanExtra(BNUtils.BNStringExtras.BNShowOthers, false);
        }

        loadSite();
        loadShowcases();
        if(showOthers) {
            loadNearPlaces();
        }

        likesListener = new BNLikesListener();
        likesListener.setListener(this);
    }

    private void loadSite(){
        imageLoader = BiinApp.getInstance().getImageLoader();
        currentSite = dataManager.getBNSite(siteIdentifier);
        if(currentSite != null) {
            RelativeLayout rlSiteLabel;
            TextView tvTitle, tvSubtitle, tvLocation;
            final ProgressBar pbSite, pbOrganization;
            final ImageView ivSite, ivSiteOrganization;

            rlSiteLabel = (RelativeLayout)findViewById(R.id.rlSiteLabel);

            ivSite = (ImageView)findViewById(R.id.ivSite);
            ivSiteOrganization = (ImageView)findViewById(R.id.ivSiteOrganization);

            tvTitle = (TextView)findViewById(R.id.tvSiteTitle);
            tvSubtitle = (TextView)findViewById(R.id.tvSiteSubtitle);
            tvLocation = (TextView)findViewById(R.id.tvSiteLocation);

            pbSite = (ProgressBar)findViewById(R.id.pbSite);
            pbOrganization = (ProgressBar)findViewById(R.id.pbSiteOrganization);

            Typeface lato_light = BNUtils.getLato_light();
            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvTitle.setTypeface(lato_black);
            tvLocation.setTypeface(lato_regular);
            tvSubtitle.setTypeface(lato_light);

            imageLoader.get(currentSite.getMedia().get(0).getUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    ivSite.setImageBitmap(response.getBitmap());
                    pbSite.setVisibility(View.GONE);
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            imageLoader.get(currentSite.getOrganization().getMedia().get(0).getUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    ivSiteOrganization.setImageBitmap(response.getBitmap());
                    pbOrganization.setVisibility(View.GONE);
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            rlSiteLabel.setBackgroundColor(currentSite.getOrganization().getPrimaryColor());
            tvTitle.setText(currentSite.getTitle());
            tvTitle.setTextColor(currentSite.getOrganization().getSecondaryColor());
            tvLocation.setText(currentSite.getSubTitle());
            tvLocation.setTextColor(currentSite.getOrganization().getSecondaryColor());
            tvSubtitle.setText(currentSite.getNutshell());
            tvSubtitle.setTextColor(currentSite.getOrganization().getSecondaryColor());

            BNToolbar toolbar = new BNToolbar(this, currentSite.getOrganization().getPrimaryColor(), currentSite.getOrganization().getSecondaryColor(), currentSite.isUserLiked(), true, true, true, true, false);
            toolbar.setListener(this);
        }else{
            Log.e(TAG, "No se encontró el site con el identifier " + siteIdentifier);
            finish();
        }
    }

    private void loadShowcases(){
        Typeface lato_regular = BNUtils.getLato_regular();
        // layout al que se va a agregar las listas de categorias
        LinearLayout layout = (LinearLayout)findViewById(R.id.vlSiteShowcases);

        // obtener la lista de showcases
        List<String> showcases = new ArrayList<>(currentSite.getShowcases());
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        for (String showcaseIdentifier : showcases) {
            // obtener el showcase por identifier
            BNShowcase showcase = dataManager.getBNShowcase(showcaseIdentifier);

            // obtener la lista de elementos de cada showcase
            List<String> elementIdentifiers = showcase.getElements();
            List<BNElement> showcaseElements = new ArrayList<>();

            if(elementIdentifiers.size() > 0) {
                for (String elementIdentifier : elementIdentifiers) {
                    BNElement element = dataManager.getBNElement(elementIdentifier);
                    if (element != null) {
                        showcaseElements.add(element);
                    }
                }
            }

            if(showcaseElements.size() > 0) {
                // poner el label (titulo) y setear el typeface
                View view = inflater.inflate(R.layout.bnshowcase_list, null);
                TextView tvShowcaseName = (TextView) view.findViewById(R.id.tvShowcaseName);
                tvShowcaseName.setTypeface(lato_regular);
                tvShowcaseName.setLetterSpacing(0.3f);
                tvShowcaseName.setText(showcase.getTitle());

                // llenar el recyclerview
                CardRecyclerView rvShowcaseList = (CardRecyclerView)view.findViewById(R.id.rvShowcaseList);
//                rvShowcaseList.setSnapEnabled(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                BNShowcaseAdapter adapter = new BNShowcaseAdapter(this, showcaseElements);
                rvShowcaseList.setLayoutManager(layoutManager);
                rvShowcaseList.setHasFixedSize(true);
                rvShowcaseList.setAdapter(adapter);

                // attach del view a la pantalla principal
                layout.addView(view);
            }
        }
    }

    private void loadNearPlaces(){
        List<String> siteIdentifiers = currentSite.getOrganization().getSites();
        siteIdentifiers.remove(currentSite.getIdentifier());

        if(siteIdentifiers.size() > 0) {
            Typeface lato_regular = BNUtils.getLato_regular();
            final List<BNSite> sites = new ArrayList<>();

            for (String siteIdentifier : siteIdentifiers) {
                BNSite site = dataManager.getBNSite(siteIdentifier);
                if (site != null) {
                    sites.add(site);
                }
            }

            TextView tvSites = (TextView) findViewById(R.id.tvSitesNearYou);
            tvSites.setText(getResources().getString(R.string.OtherPlaces1) + currentSite.getTitle() + getResources().getString(R.string.OtherPlaces2));
            tvSites.setTypeface(lato_regular);
            tvSites.setLetterSpacing(0.3f);
            tvSites.setVisibility(View.VISIBLE);

            CardRecyclerView rvSites = (CardRecyclerView) findViewById(R.id.rvSitesNearYou);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            adapter = new BNSiteAdapter(this, sites, this, rvSites, false);
            adapter.setOnLoadMoreListener(new IBNLoadMoreSitesListener(){
                @Override
                public void onLoadMoreSites(boolean isFavourites) {
                    sites.add(null);
                    adapter.notifyItemInserted(sites.size() - 1);

                    sitesListener = new BNSitesListener(sites, isFavourites);
                    sitesListener.setIdentifier(currentSite.getOrganizationIdentifier());
                    sitesListener.setListener(SitesActivity.this);

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
                    BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "MoreSitesData");
                }
            });
            rvSites.setLayoutManager(layoutManager);
            rvSites.setHasFixedSize(true);
            rvSites.setAdapter(adapter);
            rvSites.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSiteLiked(String identifier, int position) {
        likeSite(identifier, true);
    }

    @Override
    public void onSiteUnliked(String identifier, int position) {
        likeSite(identifier, false);
    }

    private void likeSite(final String identifier, final boolean liked){
        BNSite site = dataManager.getBNSite(identifier);
        site.setUserLiked(liked);

        if(liked) {
            site.setLikeDate(Calendar.getInstance().getTime());
            dataManager.addFavouriteBNSite(site, 0);
            dataManager.removeNearByBNSite(identifier);
        }else{
            site.setLikeDate(null);
            dataManager.addNearByBNSite(site, 0);
            dataManager.removeFavouriteBNSite(identifier);
        }

        String url = BNAppManager.getNetworkManagerInstance().getLikeUrl(biinie.getIdentifier(), liked);
        Log.d(TAG, url);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = site.getModel();
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
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "LikeSite");
    }

    @Override
    public void onLikeResponseOk() {

    }

    @Override
    public void onLikeResponseError() {

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

    private void onLikeError(VolleyError error, String identifier, boolean liked){
        Log.e(TAG, "Error:" + error.getMessage());
        if(liked) {
            dataManager.addPendingLikeSite(identifier);
        }else{
            dataManager.addPendingUnlikeSite(identifier);
        }
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onLike() {
        currentSite.setUserLiked(true);
        likeSite(currentSite.getIdentifier(), true);
    }

    @Override
    public void onUnlike() {
        currentSite.setUserLiked(false);
        likeSite(currentSite.getIdentifier(), false);
    }

    @Override
    public void onShare() {
        String text = BNAppManager.getNetworkManagerInstance().getUrlBase() + "/sites/" + currentSite.getIdentifier();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, text);
        i.setType("text/plain");
        startActivity(Intent.createChooser(i, getString(R.string.ActionShare)));
    }

    @Override
    public void onLocation() {

    }

    @Override
    public void onCall() {
        if(currentSite.getPhoneNumber() != null && !currentSite.getPhoneNumber().isEmpty()){
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + currentSite.getPhoneNumber()));
            startActivity(i);
        }else{
            Toast.makeText(this, R.string.NoPhone, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMail() {
        if(currentSite.getEmail() != null && !currentSite.getEmail().isEmpty()){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", currentSite.getEmail(), null));
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { currentSite.getEmail() });
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.EmailMsj));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.EmailBody));

            startActivity(Intent.createChooser(intent, getResources().getString(R.string.EmailSend)));
        }else{
            Toast.makeText(this, R.string.NoMail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShowMore() {

    }
}
