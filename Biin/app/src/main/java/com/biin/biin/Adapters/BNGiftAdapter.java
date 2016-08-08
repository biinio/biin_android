package com.biin.biin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.biin.biin.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

/**
 * Created by ramirezallan on 8/4/16.
 */
public class BNGiftAdapter extends RecyclerView.Adapter<BNGiftAdapter.SlideViewHolder> {
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private LayoutInflater inflater;

    public BNGiftAdapter(Context context) {
        viewBinderHelper.setOpenOnlyOne(true);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.bngift_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        SlideViewHolder holder = new SlideViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SlideViewHolder holder, final int position) {
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(position));
        holder.ivDelete.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Delete (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
        holder.ivShare.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Share (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
        holder.ivStores.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Stores (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {
        protected SwipeRevealLayout swipeLayout;
        protected String id;
        protected ImageView ivDelete, ivShare, ivStores;

        public SlideViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipeLayout);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivGiftDelete);
            ivShare = (ImageView) itemView.findViewById(R.id.ivGiftShare);
            ivStores = (ImageView) itemView.findViewById(R.id.ivGiftStores);
        }
    }
}
