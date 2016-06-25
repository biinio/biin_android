package com.biin.biin.Components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.biin.biin.R;

/**
 * Created by ramirezallan on 6/24/16.
 */
public class BNProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public BNProgressViewHolder(View view){
        super(view);
        progressBar = (ProgressBar)view.findViewById(R.id.pbLoadingMore);
    }
}