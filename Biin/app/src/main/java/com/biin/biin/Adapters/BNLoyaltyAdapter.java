package com.biin.biin.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biin.biin.Components.Listeners.IBNLoyaltyCardActionListener;
import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ramirezallan on 9/13/16.
 */
public class BNLoyaltyAdapter extends RecyclerView.Adapter<BNLoyaltyAdapter.BNLoyaltyViewHolder> {

    private static final String TAG = "BNLoyaltyAdapter";
    private static Context context;

    private List<BNLoyalty> loyalties;
    private ImageLoader imageLoader;
    private IBNLoyaltyCardActionListener loyaltyListener;

    public BNLoyaltyAdapter(Context context, List<BNLoyalty> loyalties, IBNLoyaltyCardActionListener loyaltyListener) {
        super();
        this.context = context;
        this.loyalties = loyalties;
        this.loyaltyListener = loyaltyListener;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BNLoyaltyAdapter.BNLoyaltyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BNLoyaltyAdapter.BNLoyaltyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return loyalties.size();
    }

    public static class BNLoyaltyViewHolder extends RecyclerView.ViewHolder {

        protected CardView cvGift;
        protected ImageView ivGift, ivDelete, ivShare, ivStores;
        protected TextView tvDate, tvName, tvDescription, tvRequest, tvDummy;

        public BNLoyaltyViewHolder(View itemView) {
            super(itemView);

            cvGift = (CardView)itemView.findViewById(R.id.cvGiftItem);

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
