package com.biin.biin.CardView;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.Typeface;
import android.graphics.drawable.VectorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.biin.biin.BNUtils;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.R;

import java.util.List;

/**
 * Created by ramirezallan on 5/11/16.
 */
public class BNSiteAdapter extends RecyclerView.Adapter<BNSiteAdapter.BNSiteViewHolder> {

    private static final String TAG = "BNSiteAdapter";
    private static Context context;

    private List<BNSite> sites;
    private ImageLoader imageLoader;

    public BNSiteAdapter(Context context, List<BNSite> sites) {
        super();
        this.context = context;
        this.sites = sites;
        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    @Override
    public BNSiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bnsite_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(BNUtils.getWidth() / 2, (BNUtils.getWidth() / 2) + (int)(56 * BNUtils.getDensity())));
        BNSiteViewHolder holder = new BNSiteViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNSiteViewHolder holder, int position) {
        BNSite item = sites.get(position);
        TableRow.LayoutParams params = new TableRow.LayoutParams(BNUtils.getWidth() / 2, (BNUtils.getWidth() / 2) + (int)(56 * BNUtils.getDensity()));

        loadSiteImage(item.getMedia().get(0).getUrl(), holder);

        holder.rlSiteLabel.setBackgroundColor(item.getOrganization().getPrimaryColor());
        holder.tvSiteTitle.setText(item.getTitle());
        holder.tvSiteTitle.setTextColor(item.getOrganization().getSecondaryColor());
        holder.tvSiteSubtitle.setText(item.getSubTitle());
        holder.tvSiteSubtitle.setTextColor(item.getOrganization().getSecondaryColor());

        if(item.isUserLiked()) {
            holder.ivLike.setVisibility(View.GONE);
            holder.ivLiked.setVisibility(View.VISIBLE);
            holder.ivLiked.setColorFilter(item.getOrganization().getSecondaryColor());
        }else {
            holder.ivLiked.setVisibility(View.GONE);
            holder.ivLike.setVisibility(View.VISIBLE);
            holder.ivLike.setColorFilter(item.getOrganization().getSecondaryColor());
        }

        holder.cvSite.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return sites.size();
    }

    public BNSite getSite(int position){
        return sites.get(position);
    }

    private void loadSiteImage(String imageURL, final BNSiteViewHolder holder) {
        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.ivSite.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public static class BNSiteViewHolder extends RecyclerView.ViewHolder{

        protected CardView cvSite;
        protected RelativeLayout rlSiteLabel;
        protected ImageView ivSite, ivLike, ivLiked;
        protected TextView tvSiteTitle, tvSiteSubtitle;

        public BNSiteViewHolder(View itemView) {
            super(itemView);

            cvSite = (CardView)itemView.findViewById(R.id.cvSite);
            rlSiteLabel = (RelativeLayout)itemView.findViewById(R.id.rlSiteLabel);

            ivSite = (ImageView)itemView.findViewById(R.id.ivSite);
            ivLike = (ImageView)itemView.findViewById(R.id.ivLike);
            ivLiked = (ImageView)itemView.findViewById(R.id.ivLiked);

            tvSiteTitle = (TextView)itemView.findViewById(R.id.tvSiteTitle);
            tvSiteSubtitle = (TextView)itemView.findViewById(R.id.tvSiteSubtitle);

            Typeface lato_light = Typeface.createFromAsset(context.getAssets(),"Lato-Light.ttf");
            Typeface lato_regular = Typeface.createFromAsset(context.getAssets(),"Lato-Regular.ttf");
            Typeface lato_black = Typeface.createFromAsset(context.getAssets(),"Lato-Black.ttf");

            tvSiteTitle.setTypeface(lato_black);
            tvSiteSubtitle.setTypeface(lato_regular);
        }
    }

}
