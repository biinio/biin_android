package com.biin.biin.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.biin.biin.ElementsActivity;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.R;

import java.util.List;

/**
 * Created by ramirezallan on 5/12/16.
 */
public class BNShowcaseAdapter extends RecyclerView.Adapter<BNShowcaseAdapter.BNShowcaseViewHolder> {

    private static final String TAG = "BNShowcaseAdapter";
    private static Context context;

    private List<BNElement> elements;
    private ImageLoader imageLoader;

    public BNShowcaseAdapter(Context context, List<BNElement> elements) {
        super();
        this.context = context;
        this.elements = elements;
        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    @Override
    public BNShowcaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bnshowcase_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int)(48 * BNUtils.getDensity())));
        BNShowcaseViewHolder holder = new BNShowcaseViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNShowcaseViewHolder holder, int position) {
        final BNElement item = elements.get(position);
        TableRow.LayoutParams params = new TableRow.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int)(48 * BNUtils.getDensity()));

        loadShowcaseElementImage(item.getMedia().get(0).getUrl(), holder);

        holder.rlShowcaseLabel.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());
        holder.tvShowcaseTitle.setText(item.getTitle());
        holder.tvShowcaseTitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
        holder.tvShowcaseSubtitle.setText(item.getSubTitle());
        holder.tvShowcaseSubtitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());

        if(item.isHasDiscount() && !item.getDiscount().isEmpty()) {
            holder.tvShowcaseOffer.setText("-" + item.getDiscount() + "%");
            holder.tvShowcaseOffer.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.ivShowcaseOffer.setColorFilter(item.getShowcase().getSite().getOrganization().getPrimaryColor());
            holder.flShowcaseOffer.setVisibility(View.VISIBLE);
        }else{
            holder.flShowcaseOffer.setVisibility(View.GONE);
        }

        holder.cvShowcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ElementsActivity.class);
                SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BNUtils.BNStringExtras.BNElement, item.getIdentifier());
                editor.commit();
                context.startActivity(i);
            }
        });

        holder.cvShowcase.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public BNElement getShowcaseElement(int position){
        return elements.get(position);
    }

    private void loadShowcaseElementImage(String imageURL, final BNShowcaseViewHolder holder) {
        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.ivShowcaseElement.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public static class BNShowcaseViewHolder extends RecyclerView.ViewHolder{

        protected CardView cvShowcase;
        protected RelativeLayout rlShowcaseLabel;
        protected FrameLayout flShowcaseOffer;
        protected ImageView ivShowcaseElement, ivShowcaseOffer;
        protected TextView tvShowcaseTitle, tvShowcaseSubtitle, tvShowcaseOffer;

        public BNShowcaseViewHolder(View itemView) {
            super(itemView);

            cvShowcase = (CardView)itemView.findViewById(R.id.cvShowcase);
            rlShowcaseLabel = (RelativeLayout)itemView.findViewById(R.id.rlShowcaseLabel);
            flShowcaseOffer = (FrameLayout)itemView.findViewById(R.id.flShowcaseOffer);

            ivShowcaseElement = (ImageView)itemView.findViewById(R.id.ivShowcaseElement);
            ivShowcaseOffer = (ImageView)itemView.findViewById(R.id.ivShowcaseOffer);

            tvShowcaseTitle = (TextView)itemView.findViewById(R.id.tvShowcaseTitle);
            tvShowcaseSubtitle = (TextView)itemView.findViewById(R.id.tvShowcaseSubtitle);
            tvShowcaseOffer = (TextView)itemView.findViewById(R.id.tvShowcaseOffer);

            Typeface lato_regular = Typeface.createFromAsset(context.getAssets(),"Lato-Regular.ttf");
            Typeface lato_black = Typeface.createFromAsset(context.getAssets(),"Lato-Black.ttf");

            tvShowcaseTitle.setTypeface(lato_black);
            tvShowcaseSubtitle.setTypeface(lato_regular);
            tvShowcaseOffer.setTypeface(lato_black);
        }
    }

}