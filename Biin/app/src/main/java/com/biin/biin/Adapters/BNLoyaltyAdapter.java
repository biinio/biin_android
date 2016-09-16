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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(BNLoyaltyAdapter.BNLoyaltyViewHolder holder, final int position) {
        viewBinderHelper.bind(holder.swipeLoyalty, String.valueOf(position));
        final BNLoyalty item = loyalties.get(position);
        final BNLoyaltyCard card = item.getLoyaltyCard();
        if(card != null) {
            final BNOrganization organization = dataManager.getBNOrganization(item.getOrganizationIdentifier());
            imageLoader.displayImage(organization.getMedia().get(0).getUrl(), holder.ivLoyalty);
            SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
            holder.tvDate.setText(showFormatter.format(card.getStartDate()));
            holder.tvName.setText(organization.getBrand());
            holder.tvDetails.setText(card.getTitle());
            holder.tvDescription.setText(card.getRule());

            if(BNUtils.calculateContrast(context.getResources().getColor(R.color.colorWhite), organization.getPrimaryColor(), organization.getSecondaryColor())) {
                holder.ivLoyalty.setBackgroundColor(organization.getPrimaryColor());
                holder.tvName.setTextColor(organization.getPrimaryColor());
            }else{
                holder.ivLoyalty.setBackgroundColor(organization.getSecondaryColor());
                holder.tvName.setTextColor(organization.getSecondaryColor());
            }

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loyaltyListener.onCardDeleted(card.getIdentifier(), position);
                }
            });
            if(card.isBiinieEnrolled()) {
                holder.hlEnrolled.setVisibility(View.VISIBLE);
                holder.tvEnroll.setVisibility(View.GONE);
                List<BNLoyaltyCard.BNLoyaltyCard_Slot> slots = card.getSlots();
                switch (slots.size()) {
                    case 12:
                        holder.ivStar11.setVisibility(View.VISIBLE);
                        holder.ivStar12.setVisibility(View.VISIBLE);
                        break;
                    case 14:
                        holder.ivStar11.setVisibility(View.VISIBLE);
                        holder.ivStar12.setVisibility(View.VISIBLE);
                        holder.ivStar13.setVisibility(View.VISIBLE);
                        holder.ivStar14.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                boolean filled = true;
                for (int i = 0; i < slots.size() && filled; i++) {
                    if(slots.get(i).isFilled()){
                        switch (i + 1){
                            case 1:
                                holder.ivStar1.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 2:
                                holder.ivStar2.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 3:
                                holder.ivStar3.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 4:
                                holder.ivStar4.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 5:
                                holder.ivStar5.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 6:
                                holder.ivStar6.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 7:
                                holder.ivStar7.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 8:
                                holder.ivStar8.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 9:
                                holder.ivStar9.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 10:
                                holder.ivStar10.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 11:
                                holder.ivStar11.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 12:
                                holder.ivStar12.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 13:
                                holder.ivStar13.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            case 14:
                                holder.ivStar14.setColorFilter(context.getResources().getColor(R.color.colorStarGold));
                                break;
                            default:
                                break;
                        }
                    }else{
                        filled = false;
                    }
                }
                holder.rlLoyaltyItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loyaltyListener.onCardClick(card.getIdentifier(), position);
                    }
                });
            }else{
                holder.hlEnrolled.setVisibility(View.GONE);
                holder.tvEnroll.setVisibility(View.VISIBLE);
                holder.rlLoyaltyItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loyaltyListener.onCardEnrolled(card.getIdentifier(), position, organization.getBrand());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return loyalties.size();
    }

    public static class BNLoyaltyViewHolder extends RecyclerView.ViewHolder {

        protected String id;
        protected RelativeLayout rlLoyaltyItem;
        protected SwipeRevealLayout swipeLoyalty;
        protected ImageView ivLoyalty, ivDelete, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5, ivStar6, ivStar7, ivStar8, ivStar9, ivStar10, ivStar11, ivStar12, ivStar13, ivStar14;
        protected TextView tvDate, tvName, tvDetails, tvDescription, tvEnroll;
        protected LinearLayout hlEnrolled;

        public BNLoyaltyViewHolder(View itemView) {
            super(itemView);

            rlLoyaltyItem = (RelativeLayout)itemView.findViewById(R.id.rlLoyaltyItem);
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
            hlEnrolled = (LinearLayout)itemView.findViewById(R.id.hlLoyaltyEnrolled);

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
