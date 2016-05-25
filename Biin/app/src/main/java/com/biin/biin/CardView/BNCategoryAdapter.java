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
public class BNCategoryAdapter extends RecyclerView.Adapter<BNCategoryAdapter.BNCategoryViewHolder> {

    private static final String TAG = "BNCategoryAdapter";
    private static Context context;

    private List<BNElement> elements;
    private ImageLoader imageLoader;

    public BNCategoryAdapter(Context context, List<BNElement> elements) {
        super();
        this.context = context;
        this.elements = elements;
        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    @Override
    public BNCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bncategory_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int)(58 * BNUtils.getDensity())));
        BNCategoryViewHolder holder = new BNCategoryViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNCategoryViewHolder holder, int position) {
        final BNElement item = elements.get(position);
        TableRow.LayoutParams params = new TableRow.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int)(58 * BNUtils.getDensity()));

        loadCategoryElementImage(item.getMedia().get(0).getUrl(), holder);

        holder.rlCategoryLabel.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());
        holder.tvCategoryElement.setText(item.getTitle());
        holder.tvCategoryElement.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
        holder.tvCategorySubtitle.setText(item.getShowcase().getSite().getTitle());
        holder.tvCategorySubtitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
        holder.tvCategoryLocation.setText(item.getShowcase().getSite().getSubTitle());
        holder.tvCategoryLocation.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
//        holder.tvCategoryPrice.setText(item.getListPrice());
        holder.tvCategoryPrice.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
        holder.tvCategoryDiscount.setText("Ȼ" + item.getPrice());
        holder.tvCategoryDiscount.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());

        if(item.isHasDiscount() && !item.getDiscount().isEmpty()) {
            holder.tvCategoryOffer.setText("-" + item.getDiscount() + "%");
            holder.tvCategoryOffer.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.ivCategoryOffer.setColorFilter(item.getShowcase().getSite().getOrganization().getPrimaryColor());
            holder.flCategoryOffer.setVisibility(View.VISIBLE);
        }else{
            holder.flCategoryOffer.setVisibility(View.GONE);
        }
        holder.cvCategory.setLayoutParams(params);

        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public BNElement getCategoryElement(int position){
        return elements.get(position);
    }

    private void loadCategoryElementImage(String imageURL, final BNCategoryViewHolder holder) {
        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.ivCategoryElement.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public static class BNCategoryViewHolder extends RecyclerView.ViewHolder{

        protected CardView cvCategory;
        protected RelativeLayout rlCategoryLabel;
        protected FrameLayout flCategoryOffer;
        protected ImageView ivCategoryElement, ivCategoryOffer;
        protected TextView tvCategoryElement, tvCategorySubtitle, tvCategoryLocation, tvCategoryPrice, tvCategoryDiscount, tvCategoryOffer;

        public BNCategoryViewHolder(View itemView) {
            super(itemView);

            cvCategory = (CardView)itemView.findViewById(R.id.cvCategory);
            rlCategoryLabel = (RelativeLayout)itemView.findViewById(R.id.rlCategoryLabel);
            flCategoryOffer = (FrameLayout)itemView.findViewById(R.id.flCategoryOffer);

            ivCategoryElement = (ImageView)itemView.findViewById(R.id.ivCategoryElement);
            ivCategoryOffer = (ImageView)itemView.findViewById(R.id.ivCategoryOffer);

            tvCategoryElement = (TextView)itemView.findViewById(R.id.tvCategoryElement);
            tvCategorySubtitle = (TextView)itemView.findViewById(R.id.tvCategorySubtitle);
            tvCategoryLocation = (TextView)itemView.findViewById(R.id.tvCategoryLocation);
            tvCategoryPrice = (TextView)itemView.findViewById(R.id.tvCategoryPrice);
            tvCategoryDiscount = (TextView)itemView.findViewById(R.id.tvCategoryDiscount);
            tvCategoryOffer = (TextView)itemView.findViewById(R.id.tvCategoryOffer);

            Typeface lato_light = Typeface.createFromAsset(context.getAssets(),"Lato-Light.ttf");
            Typeface lato_regular = Typeface.createFromAsset(context.getAssets(),"Lato-Regular.ttf");
            Typeface lato_black = Typeface.createFromAsset(context.getAssets(),"Lato-Black.ttf");

            tvCategoryElement.setTypeface(lato_black);
            tvCategorySubtitle.setTypeface(lato_black);
            tvCategoryLocation.setTypeface(lato_regular);
            tvCategoryPrice.setTypeface(lato_regular);
            tvCategoryDiscount.setTypeface(lato_regular);
            tvCategoryOffer.setTypeface(lato_black);
        }
    }

}