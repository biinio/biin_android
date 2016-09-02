package com.biin.biin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biin.biin.Adapters.BNNotificationsAdapter;
import com.biin.biin.Components.Listeners.IBNNotificationActionListener;
import com.biin.biin.Entities.BNNotification;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity implements IBNNotificationActionListener {

    private static final String TAG = "NotificationsActivity";

    private BNDataManager dataManager;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver notificationsReceiver;
    private List<BNNotification> notifications;
    private BNNotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        dataManager = BNAppManager.getInstance().getDataManagerInstance();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        notificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String type = intent.getStringExtra("TYPE");
                if(type.equals("NOTIFICATION")) {
                    Log.e(TAG, "Notificacion recibida");
                    int position = intent.getIntExtra("POSITION", 0);
                    if(adapter != null){
                        List<BNNotification> notificaciones = new ArrayList<>(dataManager.getBNNotifications());
                        adapter.addItem(notificaciones.get(position));
                        LinearLayout vlEmptyNotifications = (LinearLayout) findViewById(R.id.vlEmptyNotifications);
                        vlEmptyNotifications.setVisibility(View.GONE);
                        RecyclerView rvNotifications = (RecyclerView) findViewById(R.id.rvNotificationsList);
                        rvNotifications.scrollToPosition(0);
                    }else{
                        setUpList();
                    }
                    dataManager.clearNotificationsBadge(getApplicationContext());
                }
            }
        };

        setUpScreen();
        setUpList();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();

        TextView tvNotifications = (TextView) findViewById(R.id.tvEmptyNotifications);
        tvNotifications.setTypeface(lato_regular);
        tvNotifications.setLetterSpacing(0.3f);

        BNToolbar toolbar = new BNToolbar(this, getResources().getString(R.string.Notifications));

        dataManager.clearNotificationsBadge(getApplicationContext());
    }

    private void setUpList(){
        RecyclerView rvNotifications = (RecyclerView) findViewById(R.id.rvNotificationsList);
        LinearLayout vlEmptyNotifications = (LinearLayout) findViewById(R.id.vlEmptyNotifications);
        notifications = new ArrayList<>(dataManager.getBNNotifications());
        if (notifications.size() > 0) {
            adapter = new BNNotificationsAdapter(this, notifications, this);
            rvNotifications.setLayoutManager(new LinearLayoutManager(this));
            rvNotifications.setAdapter(adapter);
            vlEmptyNotifications.setVisibility(View.GONE);
        } else {
            vlEmptyNotifications.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("MESSAGING_SERVICE");
        localBroadcastManager.registerReceiver(notificationsReceiver, filter);
    }

    @Override
    protected void onStop() {
        localBroadcastManager.unregisterReceiver(notificationsReceiver);
        super.onStop();
    }

    @Override
    public void onNotificationDeleted(int position) {
        dataManager.removeBNNotification(position);

        if (adapter.getItemCount() == 0) {
            LinearLayout vlEmptyNotifications = (LinearLayout) findViewById(R.id.vlEmptyNotifications);
            vlEmptyNotifications.setVisibility(View.VISIBLE);
        }
    }
}
