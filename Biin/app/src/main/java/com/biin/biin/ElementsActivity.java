package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Components.Listeners.IBNToolbarListener;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Entities.BiinieAction;
import com.biin.biin.Managers.BNAnalyticsManager;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNLikesListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ElementsActivity extends AppCompatActivity implements IBNToolbarListener, BNLikesListener.IBNLikesListener {

    private static final String TAG = "ElementsActivity";

    private BNElement currentElement;
    private String elementIdentifier;
    private boolean showMore = false;

    private Biinie biinie;
    private BNDataManager dataManager;
    private BNAnalyticsManager analyticsManager;
    private BNLikesListener likesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();
        analyticsManager = BNAppManager.getInstance().getAnalyticsManagerInstance();
        biinie = dataManager.getBiinie();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        elementIdentifier = preferences.getString(BNUtils.BNStringExtras.BNElement, "element");

        Intent i = getIntent();
        if(i != null){
            showMore = i.getBooleanExtra(BNUtils.BNStringExtras.BNShowMore, false);
        }

        loadElement();

        likesListener = new BNLikesListener();
        likesListener.setListener(this);

        analyticsManager.addAction(new BiinieAction("", BiinieAction.ENTER_ELEMENT_VIEW, currentElement.getIdentifier()));
    }

    private void loadElement() {
//        imageLoader = BiinApp.getInstance().getImageLoader();
        currentElement = dataManager.getBNElement(elementIdentifier);
        if (currentElement != null) {
            // llenar el layout con la informacion
            TextView tvBefore, tvNow, tvOffer, tvTitle, tvSubtitle, tvAction, tvMore;
            final ImageView ivElementsImage, ivOffer;
            FrameLayout flOffer;
            WebView wvDetails;

            ivElementsImage = (ImageView)findViewById(R.id.ivElementsImage);
            ivOffer = (ImageView)findViewById(R.id.ivElementsOffer);
            flOffer = (FrameLayout)findViewById(R.id.flElementsOffer);
            wvDetails = (WebView)findViewById(R.id.wvElementDetails);

            tvBefore = (TextView)findViewById(R.id.tvElementsBefore);
            tvNow = (TextView)findViewById(R.id.tvElementsNow);
            tvOffer = (TextView)findViewById(R.id.tvElementsOffer);
            tvTitle = (TextView)findViewById(R.id.tvElementsTitle);
            tvSubtitle = (TextView)findViewById(R.id.tvElementsSubtitle);
            tvAction = (TextView)findViewById(R.id.tvElementsAction);

            Typeface lato_light = BNUtils.getLato_light();
            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvBefore.setTypeface(lato_light);
            tvNow.setTypeface(lato_black);
            tvOffer.setTypeface(lato_black);
            tvTitle.setTypeface(lato_black);
            tvSubtitle.setTypeface(lato_regular);
            tvAction.setTypeface(lato_black);

            int darkColor = currentElement.getShowcase().getSite().getOrganization().getPrimaryColor();
            int lightColor = currentElement.getShowcase().getSite().getOrganization().getSecondaryColor();
            if(BNUtils.calculateContrast(getResources().getColor(R.color.colorWhite), currentElement.getShowcase().getSite().getOrganization().getSecondaryColor(), darkColor)){
                darkColor = currentElement.getShowcase().getSite().getOrganization().getSecondaryColor();
                lightColor = currentElement.getShowcase().getSite().getOrganization().getPrimaryColor();
            }

            wvDetails.loadData(getHtmlBody(), "text/html;charset=UTF-8", null);
            wvDetails.setVisibility(View.VISIBLE);

            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(currentElement.getMedia().get(0).getUrl(), ivElementsImage);
//            imageLoader.get(currentElement.getMedia().get(0).getUrl(), new ImageLoader.ImageListener() {
//                @Override
//                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                    ivElementsImage.setImageBitmap(response.getBitmap());
////                    pbElement.setVisibility(View.GONE);
//                }
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                }
//            });

            if(currentElement.isHasDiscount() && !currentElement.getDiscount().isEmpty()) {
                tvOffer.setText("-" + currentElement.getDiscount() + "%");
                tvOffer.setTextColor(currentElement.getShowcase().getSite().getOrganization().getSecondaryColor());
                ivOffer.setColorFilter(currentElement.getShowcase().getSite().getOrganization().getPrimaryColor());
                flOffer.setVisibility(View.VISIBLE);
            }else{
                flOffer.setVisibility(View.GONE);
            }

            if(currentElement.isHasListPrice()) {
                tvBefore.setText(getResources().getString(R.string.Before) + " " + currentElement.getPrice());
                tvBefore.setPaintFlags(tvBefore.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                tvBefore.setText(getResources().getString(R.string.Price));
            }

            tvBefore.setTextColor(darkColor);
            tvNow.setText(getResources().getString(R.string.Now) + " " + currentElement.getListPrice());
            tvNow.setTextColor(darkColor);
            tvTitle.setText(currentElement.getTitle());
            tvSubtitle.setText(currentElement.getSubTitle());

            if(!currentElement.getCallToActionTitle().isEmpty()) {
                tvAction.setText(currentElement.getCallToActionTitle());
                tvAction.setBackgroundColor(darkColor);
                tvAction.setTextColor(lightColor);
                tvAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(currentElement.getCallToActionURL()));
                        startActivity(i);
                    }
                });
            }else{
                tvAction.setVisibility(View.GONE);
            }

            BNToolbar toolbar = new BNToolbar(this, currentElement.getShowcase().getSite().getOrganization().getPrimaryColor(), currentElement.getShowcase().getSite().getOrganization().getSecondaryColor(), currentElement.isUserLiked(), true, false, false, false, showMore, currentElement.getShowcase().getSite().getOrganization().getName());
            toolbar.setListener(this);

            tvMore = (TextView)findViewById(R.id.tvToolbarMore);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ElementsActivity.this, SitesActivity.class);
                    SharedPreferences preferences = ElementsActivity.this.getSharedPreferences(ElementsActivity.this.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(BNUtils.BNStringExtras.BNSite, currentElement.getShowcase().getSite().getIdentifier());
                    editor.commit();
                    ElementsActivity.this.startActivity(i);
                }
            });

        }else{
            Log.e(TAG, "No se encontró el elemento con el identifier " + elementIdentifier);
            finish();
        }
    }

    private String getHtmlBody() {
        String html = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><head>";
        html += "<style>";
        html += getBiinCSS("rgb(0,0,0)");
        html += "</style></head>";
        html += "<body>";
        html += currentElement.getDetailsHtml();
        html += "</body></html>";
        return html;
    }

    private String getBiinCSS(String color){
        String css = "html { font-family: Lato, Helvetica, sans-serif; background-color: rgb(238,238,238); color: " + color + "; margin-left: 5px; margin-right: 5px;}";
        css += "body { margin: 0px !important; padding: 0px !important; }";
        css += "p { font-size: 14px; font-weight:300 !important;}";
        css += "b { font-size: 14px; font-weight:500 !important;}";
        css += "li { font-size: 14px; font-weight:300 !important; margin-bottom: 5px; margin-left: -15px !important; }";
        css += "h1 { font-size: 25px; }";
        css += "h2 { font-size: 20px; }";
        css += ".biin_html{ display:table; }";
        css += ".listPrice_Table { display:table; margin:0 auto; width: 95%; }";
        css += ".listPrice_Title h2 { color: " + color + "; font-size: 20px; font-weight:300; margin-bottom: 5px; !important;}";
        css += ".listPrice { width: 100%; }";
        css += ".listPrice_Left { width: 80%; float: left; }";
        css += ".listPrice_Left_Top p{ font-size: 17px; font-weight:400; text-align: left; margin-top: 0px; margin-bottom: 0px; }";
        css += ".listPrice_Left_Bottom p{ font-size: 14px; font-weight: 200; text-align: left; color: #707070; text-overflow: ellipsis; margin-top: 0px; margin-bottom: 10px; }";
        css += ".listPrice_Right p{ width: 20%; float: right; font-size: 17px; font-weight:400; text-align: right; margin-top: 0px; margin-bottom: 0px; }";
        css += ".highlight { display:table; text-align: center; width: 100%; margin-top: 10px; }";
        css += ".highlight_title p { font-size: 20px; font-weight:300; margin-top: 0px; margin-bottom: 0px; }";
        css += ".highlight_text p { font-size: 60px; font-weight:600 !important; margin-top: -5px; margin-bottom: 20px; color: " + color + ";  line-height: 105%;}";
        css += ".highlight_subtext p { font-size: 15px; font-weight:300; margin-top: -10px; margin-bottom: 0px; }";
        css += ".biin_h2 { font-size: 25px; font-weight:500 !important; margin-top: 15px;}";
        css += ".biin_h1 { font-size: 30px; font-weight:600 !important; margin-top: 45px; margin-bottom: 10px;}";
        css += ".biin_h6 { font-size: 18px; font-weight:500 !important; }";
        css += ".biin_p { font-size: 15px; font-weight: 300 !important; }";
        css += "blockquote { border-left: 4px solid " + color + "; margin: 1.5em 10px; padding: 0.5em 10px; quotes:none;}";
        css += "blockquote:before { content: open-quote; vertical-align:middle; }";
        css += "blockquote p { font-size:25px; font-weight: 300; display: inline; }";
        return css;
    }

    @Override
    public void onLikeResponseOk() {

    }

    @Override
    public void onLikeResponseError() {

    }

    @Override
    public void onBack() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.EXIT_ELEMENT_VIEW, currentElement.getIdentifier()));
    }

    @Override
    public void onLike() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.LIKE_ELEMENT, currentElement.getIdentifier()));

        currentElement.setUserLiked(true);
        likeElement(currentElement.getIdentifier(), true);
    }

    @Override
    public void onUnlike() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.UNLIKE_ELEMENT, currentElement.getIdentifier()));

        currentElement.setUserLiked(false);
        likeElement(currentElement.getIdentifier(), false);
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
    public void onShare() {
        analyticsManager.addAction(new BiinieAction("", BiinieAction.SHARE_ELEMENT, currentElement.getIdentifier()));

        String text = BNAppManager.getInstance().getNetworkManagerInstance().getUrlBase() + "/elements/" + currentElement.getIdentifier();
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

    }

    @Override
    public void onMail() {

    }

    @Override
    public void onShowMore() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        analyticsManager.addAction(new BiinieAction("", BiinieAction.EXIT_ELEMENT_VIEW, currentElement.getIdentifier()));
    }
}
