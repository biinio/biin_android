package com.biin.biin.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biin.biin.Components.Listeners.IBNLoyaltyCardActionListener;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.Entities.BNLoyaltyCard;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ramirezallan on 9/13/16.
 */
public class BNLoyaltyAdapter extends RecyclerView.Adapter<BNLoyaltyAdapter.BNLoyaltyViewHolder> {

    private static final String TAG = "BNLoyaltyAdapter";
    private static Context context;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private List<BNLoyalty> loyalties;
    private ImageLoader imageLoader;
    private IBNLoyaltyCardActionListener loyaltyListener;
    private BNDataManager dataManager;

    public BNLoyaltyAdapter(Context context, List<BNLoyalty> loyalties, IBNLoyaltyCardActionListener loyaltyListener) {
        super();
        this.context = context;
        this.loyalties = loyalties;
        this.loyaltyListener = loyaltyListener;
        viewBinderHelper.setOpenOnlyOne(true);
        imageLoader = ImageLoader.getInstance();
        dataManager = BNAppManager.getInstance().getDataManagerInstance();
    }

    @Override
    public BNLoyaltyAdapter.BNLoyaltyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bnloyalty_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        BNLoyaltyViewHolder holder = new BNLoyaltyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNLoyaltyAdapter.BNLoyaltyViewHolder holder, int position) {
        viewBinderHelper.bind(holder.swipeLoyalty, String.valueOf(position));
        final BNLoyalty item = loyalties.get(position);
        final BNLoyaltyCard card = item.getLoyaltyCard();
        if(card != null) {
            final BNOrganization organization = dataManager.getBNOrganization(item.getOrganizationIdentifier());
            final BNElement element = dataManager.getBNElement(card.getElementIdentifier());
            imageLoader.displayImage(organization.getMedia().get(0).getUrl(), holder.ivLoyalty);
            SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
            holder.tvDate.setText(showFormatter.format(card.getStartDate()));
            holder.tvName.setText(card.getTitle());
            holder.tvDetails.setText(card.getRule());
            holder.tvDescription.setText(card.getGoal());
            holder.ivLoyalty.setBackgroundColor(organization.getPrimaryColor());
        }
    }

    @Override
    public int getItemCount() {
        return loyalties.size();
    }

    public static class BNLoyaltyViewHolder extends RecyclerView.ViewHolder {

        protected String id;
        protected CardView cvLoyalty;
        protected SwipeRevealLayout swipeLoyalty;
        protected ImageView ivLoyalty, ivDelete, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5, ivStar6, ivStar7, ivStar8, ivStar9, ivStar10, ivStar11, ivStar12, ivStar13, ivStar14;
        protected TextView tvDate, tvName, tvDetails, tvDescription, tvEnroll;
        protected LinearLayout hlEnrolled;

        public BNLoyaltyViewHolder(View itemView) {
            super(itemView);

            cvLoyalty = (CardView)itemView.findViewById(R.id.cvLoyaltyItem);
            swipeLoyalty = (SwipeRevealLayout) itemView.findViewById(R.id.swipeLoyalty);

            ivLoyalty = (ImageView) itemView.findViewById(R.id.ivLoyaltyImage);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivLoyaltyDelete);
            ivStar1 = (ImageView) itemView.findViewById(R.id.ivLoyalty1);
            ivStar2 = (ImageView) itemView.findViewById(R.id.ivLoyalty2);
            ivStar3 = (ImageView) itemView.findViewById(R.id.ivLoyalty3);
            ivStar4 = (ImageView) itemView.findViewById(R.id.ivLoyalty4);
            ivStar5 = (ImageView) itemView.findViewById(R.id.ivLoyalty5);
            ivStar6 = (ImageView) itemView.findViewById(R.id.ivLoyalty6);
            ivStar7 = (ImageView) itemView.findViewById(R.id.ivLoyalty7);
            ivStar8 = (ImageView) itemView.findViewById(R.id.ivLoyalty8);
            ivStar9 = (ImageView) itemView.findViewById(R.id.ivLoyalty9);
            ivStar10 = (ImageView) itemView.findViewById(R.id.ivLoyalty10);
            ivStar11 = (ImageView) itemView.findViewById(R.id.ivLoyalty11);
            ivStar12 = (ImageView) itemView.findViewById(R.id.ivLoyalty12);
            ivStar13 = (ImageView) itemView.findViewById(R.id.ivLoyalty13);
            ivStar14 = (ImageView) itemView.findViewById(R.id.ivLoyalty14);

            tvDate = (TextView)itemView.findViewById(R.id.tvLoyaltyDate);
            tvName = (TextView)itemView.findViewById(R.id.tvLoyaltyName);
            tvDetails = (TextView)itemView.findViewById(R.id.tvLoyaltyDetails);
            tvDescription = (TextView)itemView.findViewById(R.id.tvLoyaltyText);
            tvEnroll = (TextView)itemView.findViewById(R.id.tvLoyaltyEnroll);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvDate.setTypeface(lato_regular);
            tvName.setTypeface(lato_black);
            tvDetails.setTypeface(lato_black);
            tvDescription.setTypeface(lato_regular);
            tvEnroll.setTypeface(lato_regular);
        }
    }
}
