package com.biin.biin.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biin.biin.Components.Listeners.IBNGiftActionListener;
import com.biin.biin.Components.Listeners.IBNSitesLikeListener;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ramirezallan on 8/4/16.
 */
public class BNGiftAdapter extends RecyclerView.Adapter<BNGiftAdapter.BNGiftViewHolder> {

    private static final String TAG = "BNGiftAdapter";
    private static Context context;

    private List<BNGift> gifts;
    private ImageLoader imageLoader;
    private IBNGiftActionListener giftsListener;
    private String organizationIdentifier = "";

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private LayoutInflater inflater;

    public BNGiftAdapter(Context context, List<BNGift> gifts, IBNGiftActionListener giftsListener) {
        super();
        this.context = context;
        this.gifts = gifts;
        this.giftsListener = giftsListener;
        imageLoader = ImageLoader.getInstance();
        viewBinderHelper.setOpenOnlyOne(true);
        inflater = LayoutInflater.from(context);
    }

    public void setOrganizationIdentifier(String organizationIdentifier){
        this.organizationIdentifier = organizationIdentifier;
    }

    public void addItem(BNGift gift){
        this.gifts.add(0, gift);
    }

    @Override
    public BNGiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.bngift_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        BNGiftViewHolder holder = new BNGiftViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNGiftViewHolder holder, final int position) {
        viewBinderHelper.bind(holder.swipeGift, String.valueOf(position));
        final BNGift item = gifts.get(position);

        imageLoader.displayImage(item.getMedia().get(0).getUrl(), holder.ivGift);

        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
        holder.tvDate.setText(showFormatter.format(item.getReceivedDate()));
        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getMessage());

        holder.ivGift.setBackgroundColor(item.getPrimaryColor());
//        if(BNUtils.calculateContrast(context.getResources().getColor(R.color.colorWhite), item.getPrimaryColor(), item.getSecondaryColor())) {
//            holder.tvName.setTextColor(item.getPrimaryColor());
//            holder.tvRequest.setTextColor(item.getSecondaryColor());
//            holder.tvRequest.setBackgroundColor(item.getPrimaryColor());
//        }else{
            holder.tvName.setTextColor(item.getSecondaryColor());
            holder.tvRequest.setTextColor(item.getPrimaryColor());
            holder.tvRequest.setBackgroundColor(item.getSecondaryColor());
//        }

        String text = context.getString(R.string.SENT);
        // el beacon ha sido detectado? y es de la misma organization?
        if(item.getOrganizationIdentifier().equals(organizationIdentifier)){
            text = context.getString(R.string.READY_TO_CLAIM);
        }else{
            if(item.getStatus() == BNGift.BNGiftStatus.SENT){
                text = context.getString(R.string.SENT);
            }
        }
        holder.tvRequest.setText(text);

        holder.ivDelete.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gifts.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, gifts.size());
                    viewBinderHelper.closeLayout(String.valueOf(position));
                    giftsListener.onGiftDeleted(item.getIdentifier(), position);
                }
            }
        );
        holder.ivShare.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Share (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
        holder.ivStores.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Stores (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
        holder.tvRequest.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Request (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public static class BNGiftViewHolder extends RecyclerView.ViewHolder {

        protected String id;
        protected SwipeRevealLayout swipeGift;
        protected CardView cvGift;
        protected RelativeLayout rlGiftLabel;
        protected ImageView ivGift, ivDelete, ivShare, ivStores;
        protected TextView tvDate, tvName, tvDescription, tvRequest, tvDummy;

        public BNGiftViewHolder(View itemView) {
            super(itemView);

            cvGift = (CardView)itemView.findViewById(R.id.cvGiftItem);
            rlGiftLabel = (RelativeLayout)itemView.findViewById(R.id.rlElementLabel);
            swipeGift = (SwipeRevealLayout) itemView.findViewById(R.id.swipeGift);

            ivGift = (ImageView) itemView.findViewById(R.id.ivGiftImage);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivGiftDelete);
            ivShare = (ImageView) itemView.findViewById(R.id.ivGiftShare);
            ivStores = (ImageView) itemView.findViewById(R.id.ivGiftStores);

            tvName = (TextView)itemView.findViewById(R.id.tvGiftName);
            tvDate = (TextView)itemView.findViewById(R.id.tvGiftDate);
            tvDescription = (TextView)itemView.findViewById(R.id.tvGiftDetails);
            tvRequest = (TextView)itemView.findViewById(R.id.tvRequestGift);
            tvDummy = (TextView)itemView.findViewById(R.id.tvGiftDummy);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvName.setTypeface(lato_black);
            tvDate.setTypeface(lato_regular);
            tvDescription.setTypeface(lato_regular);
            tvRequest.setTypeface(lato_regular);
            tvDummy.setTypeface(lato_regular);
        }
    }
}


































