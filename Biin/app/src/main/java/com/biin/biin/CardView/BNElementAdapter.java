package com.biin.biin.CardView;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.R;

import java.util.List;

/**
 * Created by ramirezallan on 5/5/16.
 */
public class BNElementAdapter extends RecyclerView.Adapter<BNElementAdapter.BNElementViewHolder> {

    private static final String TAG = "BNElementsList";
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
        BNElementViewHolder holder = new BNElementViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNElementViewHolder holder, int position) {
        BNElement item = elements.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvSubtitle.setText(item.getSubTitle());
        holder.tvPrice.setText(item.getListPrice());
        holder.tvDiscount.setText(item.getPrice());

        loadElementImage("https://biinapp.blob.core.windows.net/biinmedia/97ed75ef-ce99-46eb-9b31-3f2d55f3005d/11a265c45931-42ed-b61a-fb1a0a1c2bad.jpeg", holder); //element.media.url
        loadOrganizationImage("https://biinapp.blob.core.windows.net/biinmedia/a6188431-5f5d-44e7-b654-126ae0e64bf9/media/a6188431-5f5d-44e7-b654-126ae0e64bf9/97ed75ef-ce99-46eb-9b31-3f2d55f3005d/media/ee1d96a1-855f-4d72-85ed-7977cc391515.png", holder);
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

        public ImageView ivElement, ivOrganization;
        public TextView tvTitle, tvSubtitle, tvPrice, tvDiscount;

        public BNElementViewHolder(View itemView) {
            super(itemView);
            ivElement = (ImageView)itemView.findViewById(R.id.ivElement);
            ivOrganization = (ImageView)itemView.findViewById(R.id.ivOrganization);

            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView)itemView.findViewById(R.id.tvSubtitle);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            tvDiscount = (TextView)itemView.findViewById(R.id.tvDiscount);

//            Typeface lato_rg = Typeface.createFromAsset(context.getAssets(),"Lato-Regular.ttf");
//            Typeface lato_li = Typeface.createFromAsset(context.getAssets(),"Lato-Light.ttf");
//
//            tvTitle.setTypeface(lato_rg);
//            tvSubtitle.setTypeface(lato_li);
//            tvPrice.setTypeface(lato_li);
//            tvDiscount.setTypeface(lato_rg);
        }
    }

}
