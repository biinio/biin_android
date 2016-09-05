package com.biin.biin.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biin.biin.Components.Listeners.IBNNotificationActionListener;
import com.biin.biin.Entities.BNNotification;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ramirezallan on 9/1/16.
 */
public class BNNotificationsAdapter extends RecyclerView.Adapter<BNNotificationsAdapter.BNNotificationViewHolder> {

    private static final String TAG = "BNNotificationsAdapter";
    private static Context context;

    private List<BNNotification> notifications;
    private IBNNotificationActionListener notificationsListener;

    private LayoutInflater inflater;

    public BNNotificationsAdapter(Context context, List<BNNotification> notifications, IBNNotificationActionListener notificationsListener) {
        super();
        this.context = context;
        this.notifications = notifications;
        this.notificationsListener = notificationsListener;
        inflater = LayoutInflater.from(context);
    }

    public void addItem(BNNotification notification){
        this.notifications.add(0, notification);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, notifications.size());
    }

    @Override
    public BNNotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.bnnotification_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        BNNotificationViewHolder holder = new BNNotificationViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BNNotificationViewHolder holder, final int position) {
        final BNNotification item = notifications.get(position);

        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
        holder.tvDate.setText(showFormatter.format(item.getReceivedDate()));
        holder.tvTitle.setText(item.getTitle());
        holder.tvMessage.setText(item.getMessage());

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, notifications.size() - position);
                notificationsListener.onNotificationDeleted(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class BNNotificationViewHolder extends RecyclerView.ViewHolder {

        protected RelativeLayout rlNotificationLabel;
        protected TextView tvDate, tvTitle, tvMessage;
        protected ImageView ivDelete;

        public BNNotificationViewHolder(View itemView) {
            super(itemView);

            rlNotificationLabel = (RelativeLayout)itemView.findViewById(R.id.rlNotificationLabel);

            tvTitle = (TextView)itemView.findViewById(R.id.tvNotificationTitle);
            tvDate = (TextView)itemView.findViewById(R.id.tvNotificationDate);
            tvMessage = (TextView)itemView.findViewById(R.id.tvNotificationMessage);
            ivDelete = (ImageView)itemView.findViewById(R.id.ivNotificationClose);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvTitle.setTypeface(lato_black);
            tvDate.setTypeface(lato_regular);
            tvMessage.setTypeface(lato_regular);
        }
    }
}