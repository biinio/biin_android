package com.biin.biin.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramirezallan on 7/12/16.
 */
public class BNSiteNps implements Response.Listener<JSONObject> {

    private static final String TAG = "BNSiteNps";

    private Context context;
    private Activity activity;
    private BNDataManager dataManager;

    private LinearLayout numsLayout;
    private FrameLayout commentsLayout;
    private ImageView ivLikely1, ivLikely2, ivLikely3, ivLikely4, ivLikely5, ivLikely6, ivLikely7, ivLikely8, ivLikely9, ivLikely10;
    private TextView tvLikely1, tvLikely2, tvLikely3, tvLikely4, tvLikely5, tvLikely6, tvLikely7, tvLikely8, tvLikely9, tvLikely10;
    private TextView tvTitle, tvLikely, tvNotLikely, tvQ1, tvQ2, tvQ3, tvContinue, tvSend;
    private EditText etComments;

    private BNOrganization organization;
    private BNSite site;
    private int likely;

    public BNSiteNps(Activity context, BNSite site, boolean isAvailable){
        this.context = context;
        this.activity = context;
        this.site = site;
        this.organization = site.getOrganization();
        dataManager = BNAppManager.getInstance().getDataManagerInstance();
        likely = 0;

        setUpNps(isAvailable);
    }

    private void setUpNps(boolean isAvailable){
        Typeface lato_black = BNUtils.getLato_black();
        Typeface lato_regular = BNUtils.getLato_regular();

        tvTitle = (TextView)activity.findViewById(R.id.tvNpsTitle);
        tvTitle.setTypeface(lato_regular);
        tvTitle.setLetterSpacing(0.3f);

        numsLayout = (LinearLayout)activity.findViewById(R.id.hlNpsNumbers);

        tvQ1 = (TextView)activity.findViewById(R.id.tvNpsQ1);
        tvQ3 = (TextView)activity.findViewById(R.id.tvNpsThanks);
        tvQ3.setTypeface(lato_black);

        tvLikely = (TextView)activity.findViewById(R.id.tvNpsLikely);
        tvNotLikely = (TextView)activity.findViewById(R.id.tvNpsNotLikely);
        tvContinue = (TextView)activity.findViewById(R.id.tvNpsContinue);
        tvSend = (TextView)activity.findViewById(R.id.tvNpsSend);

        if(isAvailable) {
            ivLikely1 = (ImageView) activity.findViewById(R.id.ivNps1);
            ivLikely2 = (ImageView) activity.findViewById(R.id.ivNps2);
            ivLikely3 = (ImageView) activity.findViewById(R.id.ivNps3);
            ivLikely4 = (ImageView) activity.findViewById(R.id.ivNps4);
            ivLikely5 = (ImageView) activity.findViewById(R.id.ivNps5);
            ivLikely6 = (ImageView) activity.findViewById(R.id.ivNps6);
            ivLikely7 = (ImageView) activity.findViewById(R.id.ivNps7);
            ivLikely8 = (ImageView) activity.findViewById(R.id.ivNps8);
            ivLikely9 = (ImageView) activity.findViewById(R.id.ivNps9);
            ivLikely10 = (ImageView) activity.findViewById(R.id.ivNps10);

            tvLikely1 = (TextView) activity.findViewById(R.id.tvNps1);
            tvLikely2 = (TextView) activity.findViewById(R.id.tvNps2);
            tvLikely3 = (TextView) activity.findViewById(R.id.tvNps3);
            tvLikely4 = (TextView) activity.findViewById(R.id.tvNps4);
            tvLikely5 = (TextView) activity.findViewById(R.id.tvNps5);
            tvLikely6 = (TextView) activity.findViewById(R.id.tvNps6);
            tvLikely7 = (TextView) activity.findViewById(R.id.tvNps7);
            tvLikely8 = (TextView) activity.findViewById(R.id.tvNps8);
            tvLikely9 = (TextView) activity.findViewById(R.id.tvNps9);
            tvLikely10 = (TextView) activity.findViewById(R.id.tvNps10);

            ivLikely1.setOnClickListener(likelyListener);
            ivLikely2.setOnClickListener(likelyListener);
            ivLikely3.setOnClickListener(likelyListener);
            ivLikely4.setOnClickListener(likelyListener);
            ivLikely5.setOnClickListener(likelyListener);
            ivLikely6.setOnClickListener(likelyListener);
            ivLikely7.setOnClickListener(likelyListener);
            ivLikely8.setOnClickListener(likelyListener);
            ivLikely9.setOnClickListener(likelyListener);
            ivLikely10.setOnClickListener(likelyListener);
            tvLikely1.setOnClickListener(likelyListener);
            tvLikely2.setOnClickListener(likelyListener);
            tvLikely3.setOnClickListener(likelyListener);
            tvLikely4.setOnClickListener(likelyListener);
            tvLikely5.setOnClickListener(likelyListener);
            tvLikely6.setOnClickListener(likelyListener);
            tvLikely7.setOnClickListener(likelyListener);
            tvLikely8.setOnClickListener(likelyListener);
            tvLikely9.setOnClickListener(likelyListener);
            tvLikely10.setOnClickListener(likelyListener);

            tvQ2 = (TextView)activity.findViewById(R.id.tvNpsQ2);
            tvQ2.setTypeface(lato_black);

            commentsLayout = (FrameLayout)activity.findViewById(R.id.flNpsComments);
            etComments = (EditText) activity.findViewById(R.id.etNpsComments);
            etComments.setTypeface(lato_regular);

            tvLikely.setTypeface(lato_black);
            tvNotLikely.setTypeface(lato_black);
            tvQ1.setTypeface(lato_black);
            tvContinue.setTypeface(lato_black);
            tvSend.setTypeface(lato_black);

            tvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentsLayout.setVisibility(View.GONE);
                    tvQ2.setVisibility(View.GONE);
                    tvSend.setVisibility(View.INVISIBLE);
                    tvSend.setOnClickListener(null);
                    tvQ3.setVisibility(View.VISIBLE);

                    String url = BNAppManager.getInstance().getNetworkManagerInstance().getNpsUrl(BNAppManager.getInstance().getDataManagerInstance().getBiinie().getIdentifier(), organization.getIdentifier());
                    Log.d(TAG, url);

                    JSONObject request = new JSONObject();
                    try {
                        JSONObject model = new JSONObject();
                        model.put("points", likely);
                        request.put("model", model);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error:" + e.getMessage());
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.PUT,
                            url,
                            request,
                            BNSiteNps.this,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "Error:" + error.getMessage());
                                    dataManager.removeNpsDateTime(site.getIdentifier());
                                }
                            });
                    dataManager.setNpsDateTime(site.getIdentifier());
                    BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "SiteNPS");
                }
            });
        }else{
            tvQ1.setVisibility(View.GONE);
            numsLayout.setVisibility(View.GONE);
            tvLikely.setVisibility(View.GONE);
            tvNotLikely.setVisibility(View.GONE);
            tvContinue.setVisibility(View.GONE);
            tvSend.setVisibility(View.INVISIBLE);
            tvQ3.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener likelyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ivNps1:
                case R.id.tvNps1:
                    if(markLikely(1)){
                        ivLikely1.setColorFilter(context.getResources().getColor(R.color.colorLikely1));
                        tvLikely1.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps2:
                case R.id.tvNps2:
                    if(markLikely(2)){
                        ivLikely2.setColorFilter(context.getResources().getColor(R.color.colorLikely2));
                        tvLikely2.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps3:
                case R.id.tvNps3:
                    if(markLikely(3)){
                        ivLikely3.setColorFilter(context.getResources().getColor(R.color.colorLikely3));
                        tvLikely3.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps4:
                case R.id.tvNps4:
                    if(markLikely(4)){
                        ivLikely4.setColorFilter(context.getResources().getColor(R.color.colorLikely4));
                        tvLikely4.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps5:
                case R.id.tvNps5:
                    if(markLikely(5)){
                        ivLikely5.setColorFilter(context.getResources().getColor(R.color.colorLikely5));
                        tvLikely5.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps6:
                case R.id.tvNps6:
                    if(markLikely(6)){
                        ivLikely6.setColorFilter(context.getResources().getColor(R.color.colorLikely6));
                        tvLikely6.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps7:
                case R.id.tvNps7:
                    if(markLikely(7)){
                        ivLikely7.setColorFilter(context.getResources().getColor(R.color.colorLikely7));
                        tvLikely7.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps8:
                case R.id.tvNps8:
                    if(markLikely(8)){
                        ivLikely8.setColorFilter(context.getResources().getColor(R.color.colorLikely8));
                        tvLikely8.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps9:
                case R.id.tvNps9:
                    if(markLikely(9)){
                        ivLikely9.setColorFilter(context.getResources().getColor(R.color.colorLikely9));
                        tvLikely9.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
                case R.id.ivNps10:
                case R.id.tvNps10:
                    if(markLikely(10)){
                        ivLikely10.setColorFilter(context.getResources().getColor(R.color.colorLikely10));
                        tvLikely10.setTextColor(context.getResources().getColor(R.color.colorLightGrey));
                    }
                    break;
            }
        }
    };

    private boolean markLikely(int likely){
        int prev;

        if(this.likely == likely){
            prev = likely;
            this.likely = 0;
            tvContinue.setOnClickListener(null);
            tvContinue.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }else{
            prev = this.likely;
            this.likely = likely;
            tvContinue.setOnClickListener(continueListener);
            tvContinue.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }

        switch (prev){
            case 0:
                break;
            case 1:
                ivLikely1.clearColorFilter();
                tvLikely1.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 2:
                ivLikely2.clearColorFilter();
                tvLikely2.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 3:
                ivLikely3.clearColorFilter();
                tvLikely3.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 4:
                ivLikely4.clearColorFilter();
                tvLikely4.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 5:
                ivLikely5.clearColorFilter();
                tvLikely5.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 6:
                ivLikely6.clearColorFilter();
                tvLikely6.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 7:
                ivLikely7.clearColorFilter();
                tvLikely7.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 8:
                ivLikely8.clearColorFilter();
                tvLikely8.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 9:
                ivLikely9.clearColorFilter();
                tvLikely9.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 10:
                ivLikely10.clearColorFilter();
                tvLikely10.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
        }

        return likely != prev;
    }

    private View.OnClickListener continueListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            numsLayout.setVisibility(View.GONE);
            tvQ1.setVisibility(View.GONE);
            tvQ2.setVisibility(View.VISIBLE);
            tvLikely.setVisibility(View.GONE);
            tvNotLikely.setVisibility(View.GONE);
            commentsLayout.setVisibility(View.VISIBLE);
            tvContinue.setVisibility(View.GONE);
            tvSend.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onResponse(JSONObject response) {
        //TODO si da error, marcar el NPS para volver a aparecer, sino no
    }
}
