package com.biin.biin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biin.biin.Adapters.BNFriendsAdapter;
import com.biin.biin.Components.Listeners.IBNFriendsListener;
import com.biin.biin.Entities.BNFriend;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements IBNFriendsListener {

    private static final String TAG = "FriendsActivity";
    private static final int RESULT = 1003;

    private List<BNFriend> friends;
    private BNFriendsAdapter adapter;
    private String giftIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        giftIdentifier = getIntent().getStringExtra(BNUtils.BNStringExtras.BNGift);

        setUpScreen();
        setUpList();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvFriends = (TextView) findViewById(R.id.tvEmptyFriends);
        tvFriends.setTypeface(lato_regular);
        tvFriends.setLetterSpacing(0.3f);

        BNToolbar toolbar = new BNToolbar(this, getResources().getString(R.string.MyFriends));
    }

    private void setUpList(){
        RecyclerView rvFriends = (RecyclerView) findViewById(R.id.rvFriendsList);
        LinearLayout vlEmptyFriends = (LinearLayout) findViewById(R.id.vlAddFriends);
        friends = new ArrayList<>(BNAppManager.getInstance().getDataManagerInstance().getBiinie().getFacebookFriends());
        if (friends.size() > 0) {
            adapter = new BNFriendsAdapter(this, friends, this);
            rvFriends.setLayoutManager(new LinearLayoutManager(this));
            rvFriends.setAdapter(adapter);
            vlEmptyFriends.setVisibility(View.GONE);
        } else {
            vlEmptyFriends.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFriendSelected(String facebookId) {
        Intent intent = new Intent();
        intent.putExtra(BNUtils.BNStringExtras.BNFacebook, facebookId);
        intent.putExtra(BNUtils.BNStringExtras.BNGift, giftIdentifier);
        setResult(RESULT, intent);
        finish();
    }
}
