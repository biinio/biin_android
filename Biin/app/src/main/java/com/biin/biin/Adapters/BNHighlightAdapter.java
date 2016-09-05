package com.biin.biin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biin.biin.ElementsActivity;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramirezallan on 6/17/16.
 */
public class BNHighlightAdapter extends LoopPagerAdapter {

    private static Context context;

    private List<BNElement> highlights = new ArrayList<>();
    private ImageLoader imageLoader;

    public BNHighlightAdapter(RollPagerView viewPager, Context context) {
        super(viewPager);
        this.context = context;
        imageLoader = ImageLoader.getInstance();
    }

    public void setHighlights(List<BNElement> elements){
        highlights = elements;
    }

    @Override
    public View getView(final ViewGroup container, int position) {
        final BNElement element = highlights.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bnelement_item, null);

        RelativeLayout rlElementLabel;
        FrameLayout flOffer;
        TextView tvTitle, tvSubtitle, tvSubtitleLocation, tvPrice, tvDiscount, tvOffer;
        ImageView ivOffer;
        final ImageView ivElement, ivOrganization;

        rlElementLabel = (RelativeLayout)view.findViewById(R.id.rlElementLabel);
        flOffer = (FrameLayout)view.findViewById(R.id.flElementOffer);

        ivElement = (ImageView)view.findViewById(R.id.ivElement);
        ivOrganization = (ImageView)view.findViewById(R.id.ivFriendAvatar);
        ivOffer = (ImageView)view.findViewById(R.id.ivElementOffer);

        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvSubtitle = (TextView)view.findViewById(R.id.tvOfferSubtitle);
        tvSubtitleLocation = (TextView)view.findViewById(R.id.tvSubtitleLocation);
        tvPrice = (TextView)view.findViewById(R.id.tvPrice);
        tvDiscount = (TextView)view.findViewById(R.id.tvDiscount);
        tvOffer = (TextView)view.findViewById(R.id.tvElementOffer);

        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        tvTitle.setTypeface(lato_regular);
        tvSubtitle.setTypeface(lato_black);
        tvSubtitleLocation.setTypeface(lato_regular);
        tvPrice.setTypeface(lato_regular);
        tvDiscount.setTypeface(lato_regular);
        tvOffer.setTypeface(lato_black);

        imageLoader.displayImage(element.getMedia().get(0).getUrl(), ivElement);
        ivElement.setBackgroundColor(element.getShowcase().getSite().getOrganization().getPrimaryColor());

        imageLoader.displayImage(element.getShowcase().getSite().getMedia().get(0).getUrl(), ivOrganization);
        ivOrganization.setBackgroundColor(element.getShowcase().getSite().getOrganization().getPrimaryColor());

        rlElementLabel.setBackgroundColor(element.getShowcase().getSite().getOrganization().getPrimaryColor());
        tvTitle.setText(element.getTitle());
        tvTitle.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
        tvSubtitle.setText(element.getShowcase().getSite().getTitle());
        tvSubtitle.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
        tvSubtitleLocation.setText(element.getShowcase().getSite().getSubTitle());
        tvSubtitleLocation.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
        tvPrice.setText(element.getPriceTitle(context, false));
        tvPrice.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
        tvDiscount.setText(element.getPriceSubtitle(context, false));
        tvDiscount.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());

        if(element.isHasDiscount() && !element.getDiscount().isEmpty()) {
            tvOffer.setText("-" + element.getDiscount() + "%");
            tvOffer.setTextColor(element.getShowcase().getSite().getOrganization().getSecondaryColor());
            ivOffer.setColorFilter(element.getShowcase().getSite().getOrganization().getPrimaryColor());
            flOffer.setVisibility(View.VISIBLE);
        }else{
            flOffer.setVisibility(View.GONE);
        }

        view.setLayoutParams(new RelativeLayout.LayoutParams(BNUtils.getWidth(), BNUtils.getWidth() + (int)(68 * BNUtils.getDensity())));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ElementsActivity.class);
                SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BNUtils.BNStringExtras.BNElement, element.getIdentifier());
                editor.commit();
                i.putExtra(BNUtils.BNStringExtras.BNShowMore, true);
                context.startActivity(i);
            }
        });
        return view;
    }

    @Override
    protected int getRealCount() {
        return highlights.size();
    }
}
