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

import com.biin.biin.Components.Listeners.IBNFriendsListener;
import com.biin.biin.Entities.BNFriend;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ramirezallan on 9/5/16.
 */
public class BNFriendsAdapter extends RecyclerView.Adapter<BNFriendsAdapter.BNFriendsViewHolder> {

    private static final String TAG = "BNFriendsAdapter";
    private static Context context;

    private ImageLoader imageLoader;
    private List<BNFriend> friends;
    private IBNFriendsListener friendsListener;

    public BNFriendsAdapter(Context context, List<BNFriend> friends, IBNFriendsListener friendsListener) {
        super();
        this.context = context;
        this.friends = friends;
        this.friendsListener = friendsListener;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BNFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bnfriend_item, parent, false);
        BNFriendsViewHolder holder = new BNFriendsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BNFriendsViewHolder holder, int position) {
        final BNFriend item = friends.get(position);
        imageLoader.displayImage(item.getFacebookAvatarUrl(), holder.ivAvatar);
        holder.tvName.setText(item.getFacebookName());
        holder.rlFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendsListener.onFriendSelected(item.getFacebook_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public BNFriend getFriend(int position){
        return friends.get(position);
    }

    public static class BNFriendsViewHolder extends RecyclerView.ViewHolder {

        protected RelativeLayout rlFriend;
        protected ImageView ivAvatar;
        protected TextView tvName;

        public BNFriendsViewHolder(View itemView) {
            super(itemView);

            rlFriend = (RelativeLayout)itemView.findViewById(R.id.rlFriend);
            ivAvatar = (ImageView)itemView.findViewById(R.id.ivFriendAvatar);
            tvName = (TextView)itemView.findViewById(R.id.tvFriendName);

            Typeface lato_black = BNUtils.getLato_black();

            tvName.setTypeface(lato_black);
        }
    }
}
