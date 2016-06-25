package com.biin.biin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import com.android.volley.toolbox.NetworkImageView;
import com.biin.biin.Components.Listeners.BNAdapterListener.BNSitesLikeListener;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Utils.BNUtils.BNStringExtras;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.R;
import com.biin.biin.SitesActivity;

import java.util.List;

/**
 * Created by ramirezallan on 5/11/16.
 */
public class BNSiteAdapter extends RecyclerView.Adapter<BNSiteAdapter.BNSiteViewHolder> {

    private static final String TAG = "BNSiteAdapter";
    private static Context context;

    private List<BNSite> sites;
    private ImageLoader imageLoader;
    private boolean showOthers = false;
    private BNSitesLikeListener sitesListener;

    public BNSiteAdapter(Context context, List<BNSite> sites, BNSitesLikeListener sitesListener) {
        super();
        this.context = context;
        this.sites = sites;
        this.sitesListener = sitesListener;
        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    public void setShowOthers(boolean showOthers){
        this.showOthers = showOthers;
    }

    @Override
    public BNSiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bnsite_item_small, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams((sites.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int)(56 * BNUtils.getDensity())));
        BNSiteViewHolder holder = new BNSiteViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNSiteViewHolder holder, final int position) {
        final BNSite item = sites.get(position);
        TableRow.LayoutParams params = new TableRow.LayoutParams((sites.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int)(56 * BNUtils.getDensity()));

//        loadSiteImage(item.getMedia().get(0).getUrl(), holder);
        imageLoader.get(item.getMedia().get(0).getUrl(), ImageLoader.getImageListener(holder.ivSite, R.drawable.bg_feedback, R.drawable.biin));
        holder.ivSite.setImageUrl(item.getMedia().get(0).getUrl(), imageLoader);
        holder.ivSite.setBackgroundColor(item.getOrganization().getPrimaryColor());

        holder.rlSiteLabel.setBackgroundColor(item.getOrganization().getPrimaryColor());
        holder.tvSiteTitle.setText(item.getTitle());
        holder.tvSiteTitle.setTextColor(item.getOrganization().getSecondaryColor());
        holder.tvSiteSubtitle.setText(item.getSubTitle());
        holder.tvSiteSubtitle.setTextColor(item.getOrganization().getSecondaryColor());
        holder.cvSite.setLayoutParams(params);

        if(item.isUserLiked()) {
            holder.ivLike.setVisibility(View.GONE);
            holder.ivLiked.setVisibility(View.VISIBLE);
            holder.ivLiked.setColorFilter(item.getOrganization().getSecondaryColor());
            holder.ivLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // quitar el like y cambiarlo de lista
                    sitesListener.onSiteUnliked(item.getIdentifier());
                }
            });
        }else {
            holder.ivLiked.setVisibility(View.GONE);
            holder.ivLike.setVisibility(View.VISIBLE);
            holder.ivLike.setColorFilter(item.getOrganization().getSecondaryColor());
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // darle like y cambiarlo de lista
                    sitesListener.onSiteLiked(item.getIdentifier());
                }
            });
        }

        holder.cvSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SitesActivity.class);
                SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BNStringExtras.BNSite, item.getIdentifier());
                editor.commit();
                i.putExtra(BNStringExtras.BNShowOthers, showOthers);
                context.startActivity(i);
            }
        });
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
        protected ImageView ivLike, ivLiked;
        protected NetworkImageView ivSite;
        protected TextView tvSiteTitle, tvSiteSubtitle;

        public BNSiteViewHolder(View itemView) {
            super(itemView);

            cvSite = (CardView)itemView.findViewById(R.id.cvSite);
            rlSiteLabel = (RelativeLayout)itemView.findViewById(R.id.rlSiteLabel);

            ivSite = (NetworkImageView)itemView.findViewById(R.id.ivSiteSmall);
            ivLike = (ImageView)itemView.findViewById(R.id.ivLike);
            ivLiked = (ImageView)itemView.findViewById(R.id.ivLiked);

            tvSiteTitle = (TextView)itemView.findViewById(R.id.tvSiteTitle);
            tvSiteSubtitle = (TextView)itemView.findViewById(R.id.tvSiteSubtitle);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvSiteTitle.setTypeface(lato_black);
            tvSiteSubtitle.setTypeface(lato_regular);
        }
    }

}
