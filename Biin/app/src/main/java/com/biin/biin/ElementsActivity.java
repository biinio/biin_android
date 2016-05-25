package com.biin.biin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;

public class ElementsActivity extends AppCompatActivity {

    private static final String TAG = "ElementsActivity";

    private BNElement currentElement;
    private String elementIdentifier;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        elementIdentifier = preferences.getString(BNUtils.BNStringExtras.BNElement, "element");

        loadElement();
    }

    private void loadElement() {
        imageLoader = BiinApp.getInstance().getImageLoader();
        currentElement = BNAppManager.getDataManagerInstance().getBNElement(elementIdentifier);
        if (currentElement != null) {
            //TODO llenar el layout con la informacion
            TextView tvBefore, tvNow, tvOffer, tvTitle, tvSubtitle, tvDetails, tvAction;
            final ImageView ivElementsImage, ivOffer;
            FrameLayout flOffer;

            ivElementsImage = (ImageView)findViewById(R.id.ivElementsImage);
            ivOffer = (ImageView)findViewById(R.id.ivElementsOffer);
            flOffer = (FrameLayout)findViewById(R.id.flElementsOffer);

            tvBefore = (TextView)findViewById(R.id.tvElementsBefore);
            tvNow = (TextView)findViewById(R.id.tvElementsNow);
            tvOffer = (TextView)findViewById(R.id.tvElementsOffer);
            tvTitle = (TextView)findViewById(R.id.tvElementsTitle);
            tvSubtitle = (TextView)findViewById(R.id.tvElementsSubtitle);
            tvDetails = (TextView)findViewById(R.id.tvElementsDetails);
            tvAction = (TextView)findViewById(R.id.tvElementsAction);

            Typeface lato_light = Typeface.createFromAsset(getAssets(),"Lato-Light.ttf");
            Typeface lato_regular = Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
            Typeface lato_black = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");

            tvBefore.setTypeface(lato_light);
            tvNow.setTypeface(lato_black);
            tvOffer.setTypeface(lato_black);
            tvTitle.setTypeface(lato_black);
            tvSubtitle.setTypeface(lato_regular);
            tvDetails.setTypeface(lato_regular);
            tvAction.setTypeface(lato_black);

            int darkColor = currentElement.getShowcase().getSite().getOrganization().getPrimaryColor();
            int lightColor = currentElement.getShowcase().getSite().getOrganization().getSecondaryColor();
            if(BNUtils.calculateContrast(getResources().getColor(R.color.colorWhite), currentElement.getShowcase().getSite().getOrganization().getSecondaryColor(), darkColor)){
                darkColor = currentElement.getShowcase().getSite().getOrganization().getSecondaryColor();
                lightColor = currentElement.getShowcase().getSite().getOrganization().getPrimaryColor();
            }

            imageLoader.get(currentElement.getMedia().get(0).getUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    ivElementsImage.setImageBitmap(response.getBitmap());
//                    pbElement.setVisibility(View.GONE);
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

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
            tvDetails.setText(currentElement.getNutshellDescriptionTitle());

            if(!currentElement.getCallToActionTitle().isEmpty()) {
                tvAction.setText(currentElement.getCallToActionTitle());
                tvAction.setBackgroundColor(darkColor);
                tvAction.setTextColor(lightColor);
            }else{
                tvAction.setVisibility(View.GONE);
            }
            new BNToolbar(this, currentElement.getShowcase().getSite().getOrganization().getPrimaryColor(), currentElement.getShowcase().getSite().getOrganization().getSecondaryColor(), currentElement.getShowcase().getSite().isUserLiked(), true, false, false, false, true, currentElement.getShowcase().getSite().getOrganization().getName());
        }else{
            Log.e(TAG, "No se encontró el site con el identifier " + elementIdentifier);
            finish();
        }
    }
}
