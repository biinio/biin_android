package com.biin.biin;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by ramirezallan on 8/9/16.
 */
public class BiinInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "BiinInstanceIdService";
    private static final String TOPIC = "biin";

    /**
     * The Application's current Instance ID token is no longer valid
     * and thus a new one must be requested.
     */
    @Override
    public void onTokenRefresh() {
        // If you need to handle the generation of a token, initially or
        // after a refresh this is where you should do that.
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);

        // Once a token is generated, we subscribe to topic.
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
    }

    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "FCM Token: " + token);
        // TODO: Implement this method to send token to your app server.
    }
}