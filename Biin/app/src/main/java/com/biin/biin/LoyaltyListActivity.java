package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Adapters.BNLoyaltyAdapter;
import com.biin.biin.Components.Listeners.IBNLoyaltyCardActionListener;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyListActivity extends AppCompatActivity implements IBNLoyaltyCardActionListener {

    private static final String TAG = "LoyaltyListActivity";

    private BNDataManager dataManager;
    private List<BNLoyalty> loyalties;
    private BNLoyaltyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_list);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();

        setUpScreen();
        setUpList();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvLoyalties = (TextView) findViewById(R.id.tvEmptyLoyalties);
        tvLoyalties.setTypeface(lato_regular);
        tvLoyalties.setLetterSpacing(0.3f);

        BNToolbar toolbar = new BNToolbar(this, getResources().getString(R.string.LoyaltyWallet));
    }

    private void setUpList(){
        RecyclerView rvSlides = (RecyclerView) findViewById(R.id.rvLoyaltiesList);
        LinearLayout vlEmptyLoyalties = (LinearLayout) findViewById(R.id.vlEmptyLoyalties);
        loyalties = new ArrayList<>(dataManager.getBNLoyalties());
        if (loyalties.size() > 0) {
            adapter = new BNLoyaltyAdapter(this, loyalties, this);
            rvSlides.setLayoutManager(new LinearLayoutManager(this));
            rvSlides.setAdapter(adapter);
            vlEmptyLoyalties.setVisibility(View.GONE);
        } else {
            vlEmptyLoyalties.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCardClick(String identifier, int position) {
        Intent i = new Intent(this, LoyaltyActivity.class);
        i.putExtra(BNUtils.BNStringExtras.BNLoyalty, identifier);
        i.putExtra(BNUtils.BNStringExtras.Position, position);
        startActivity(i);
    }

    @Override
    public void onCardEnrolled(final String identifier, final int position, String name) {
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.dialog_popup, null);
        ((TextView)dialogView.findViewById(R.id.tvPopUpTitle)).setText(getString(R.string.TermOfUser));
        ((TextView)dialogView.findViewById(R.id.tvPopUpMessage)).setText(getString(R.string.LoyaltyCardConditions) + name);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        dialogView.findViewById(R.id.ivPopupClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tvPopupConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // request enroll
                enrollCard(identifier, position);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void enrollCard(final String cardIdentifier, final int position){
        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();
        String identifier;

        if(biinie != null && biinie.getIdentifier() != null && !biinie.getIdentifier().isEmpty()){
            identifier = biinie.getIdentifier();
        }else {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
            identifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getInstance().getNetworkManagerInstance().getLoyaltyEnrollUrl(identifier, cardIdentifier),
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Response:" + response.toString());
                        try {
                            String result = response.getString("result");
                            String status = response.getString("status");
                            if(status.equals("0") && result.equals("1")){
                                Intent i = new Intent(LoyaltyListActivity.this, LoyaltyActivity.class);
                                i.putExtra(BNUtils.BNStringExtras.BNLoyalty, cardIdentifier);
                                i.putExtra(BNUtils.BNStringExtras.Position, position);
                                loyalties.get(position).getLoyaltyCard().setBiinieEnrolled(true);
                                adapter.notifyItemChanged(position);
                                startActivity(i);
                            }else{
                                Log.e(TAG, "Error enlistandose en la tarjeta.");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parseando el JSON.", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
    }

    @Override
    public void onCardDeleted(String identifier, int position) {
        Toast.makeText(this, "Delete card, show popup", Toast.LENGTH_SHORT).show();
    }
}
