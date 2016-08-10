package com.biin.biin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class BiinMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BiinMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.e(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.e(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.e(TAG, "FCM Data Message: " + remoteMessage.getData());
    }
}