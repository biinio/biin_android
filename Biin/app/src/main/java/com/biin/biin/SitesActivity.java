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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Utils.BNUtils.BNStringExtras;
import com.biin.biin.CardView.BNShowcaseAdapter;
import com.biin.biin.CardView.BNSiteAdapter;
import com.biin.biin.CardView.CardRecyclerView;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;

import java.util.ArrayList;
import java.util.List;

public class SitesActivity extends AppCompatActivity {

    private static final String TAG = "SitesActivity";

    private BNSite currentSite;
    private String siteIdentifier;
    private ImageLoader imageLoader;
    private boolean showOthers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);

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

        (findViewById(R.id.nvSites)).scrollTo(0,0);
    }

    private void loadSite(){
        imageLoader = BiinApp.getInstance().getImageLoader();
        currentSite = BNAppManager.getDataManagerInstance().getBNSite(siteIdentifier);
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

            Typeface lato_light = Typeface.createFromAsset(getAssets(),"Lato-Light.ttf");
            Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
            Typeface lato_black = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");

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

            new BNToolbar(this, currentSite.getOrganization().getPrimaryColor(), currentSite.getOrganization().getSecondaryColor(), currentSite.isUserLiked(), true, true, true, true, false);
        }else{
            Log.e(TAG, "No se encontró el site con el identifier " + siteIdentifier);
            finish();
        }
    }

    private void loadShowcases(){
        BNDataManager dataManager = BNAppManager.getDataManagerInstance();
        Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
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
        List<String> siteIdentifiers = new ArrayList<>(currentSite.getOrganization().getSites());
        siteIdentifiers.remove(currentSite.getIdentifier());

        if(siteIdentifiers.size() > 0) {
            Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
            BNDataManager dataManager = BNAppManager.getDataManagerInstance();
            List<BNSite> sites = new ArrayList<>();

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

            BNSiteAdapter adapter = new BNSiteAdapter(this, sites);
            rvSites.setLayoutManager(layoutManager);
            rvSites.setHasFixedSize(true);
            rvSites.setAdapter(adapter);
            rvSites.setVisibility(View.VISIBLE);
        }
    }

}
