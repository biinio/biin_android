package com.biin.biin.CardView;

import android.content.Context;
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
import com.biin.biin.BNUtils;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.R;

import java.util.List;

/**
 * Created by ramirezallan on 5/5/16.
 */
public class BNElementAdapter extends RecyclerView.Adapter<BNElementAdapter.BNElementViewHolder> {

    private static final String TAG = "BNElementAdapter";
    private static Context context;

    private List<BNElement> elements;
    private ImageLoader imageLoader;

    public BNElementAdapter(Context context, List<BNElement> elements) {
        super();
        this.context = context;
        this.elements = elements;
        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    @Override
    public BNElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bnelement_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(BNUtils.getWidth(), BNUtils.getWidth() + (int)(68 * BNUtils.getDensity())));
        BNElementViewHolder holder = new BNElementViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNElementViewHolder holder, int position) {
        BNElement item = elements.get(position);
        TableRow.LayoutParams params = new TableRow.LayoutParams(BNUtils.getWidth(), BNUtils.getWidth() + (int)(68 * BNUtils.getDensity()));

        loadElementImage(item.getMedia().get(0).getUrl(), holder); //element.media.url
        loadOrganizationImage(item.getShowcase().getSite().getMedia().get(0).getUrl(), holder);

        holder.rlElementLabel.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());
        holder.tvTitle.setText(item.getTitle());
        holder.tvTitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
        holder.tvSubtitle.setText(item.getShowcase().getSite().getTitle());
        holder.tvSubtitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
        holder.tvSubtitleLocation.setText(item.getShowcase().getSite().getSubTitle());
        holder.tvSubtitleLocation.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
//        holder.tvPrice.setText(item.getListPrice());
        holder.tvPrice.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
        holder.tvDiscount.setText("Ȼ" + item.getPrice());
        holder.tvDiscount.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());

        holder.cvElement.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public BNElement getElement(int position){
        return elements.get(position);
    }

    private void loadElementImage(String imageURL, final BNElementViewHolder holder) {
        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.ivElement.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void loadOrganizationImage(String imageURL, final BNElementViewHolder holder) {
        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.ivOrganization.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public static class BNElementViewHolder extends RecyclerView.ViewHolder{

        protected RelativeLayout rlElementLabel;
        protected ImageView ivElement, ivOrganization;
        protected TextView tvTitle, tvSubtitle, tvSubtitleLocation, tvPrice, tvDiscount;
        protected CardView cvElement;

        public BNElementViewHolder(View itemView) {
            super(itemView);

            cvElement = (CardView)itemView.findViewById(R.id.cvElement);
            rlElementLabel = (RelativeLayout)itemView.findViewById(R.id.rlElementLabel);

            ivElement = (ImageView)itemView.findViewById(R.id.ivElement);
            ivOrganization = (ImageView)itemView.findViewById(R.id.ivOrganization);

            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView)itemView.findViewById(R.id.tvSubtitle);
            tvSubtitleLocation = (TextView)itemView.findViewById(R.id.tvSubtitleLocation);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            tvDiscount = (TextView)itemView.findViewById(R.id.tvDiscount);

            Typeface lato_light = Typeface.createFromAsset(context.getAssets(),"Lato-Light.ttf");
            Typeface lato_regular = Typeface.createFromAsset(context.getAssets(),"Lato-Regular.ttf");
            Typeface lato_black = Typeface.createFromAsset(context.getAssets(),"Lato-Black.ttf");

            tvTitle.setTypeface(lato_regular);
            tvSubtitle.setTypeface(lato_black);
            tvSubtitleLocation.setTypeface(lato_regular);
            tvPrice.setTypeface(lato_regular);
            tvDiscount.setTypeface(lato_regular);
        }
    }

}
