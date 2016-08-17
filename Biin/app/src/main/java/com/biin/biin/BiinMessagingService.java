package com.biin.biin;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.biin.biin.Entities.BNGift;
import com.biin.biin.Entities.BNJSONParsers.BNGiftParser;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class BiinMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BiinMessagingService";
    private static BNGiftParser giftParser = new BNGiftParser();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.e(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.e(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.e(TAG, "FCM Data Message: " + remoteMessage.getData());

        if(true) { //TODO determinar si es una notificacion o un gift (por el momento true porque solo son gifts)
            BNGift gift = parseGift(remoteMessage.getData().toString());
            if (gift != null) {
                BNDataManager dataManager = BNAppManager.getInstance().getDataManagerInstance();
                if (dataManager.addBNGift(gift)) {
                    dataManager.incrementGiftsBadge();
                    // llamar al broadcast para avisar a la pantalla principal
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                    Intent intent = new Intent("MESSAGING_SERVICE");
                    intent.putExtra("TYPE", "GIFT");
                    localBroadcastManager.sendBroadcast(intent);
                }
            } else {
                Log.e(TAG, "Error: no Ne pudo parsear el gift (" + remoteMessage.getData() + ")");
            }
        }else{
            //Notificacion solamente
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            Intent intent = new Intent("MESSAGING_SERVICE");
            intent.putExtra("TYPE", "NOTIFICATION");
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    private BNGift parseGift(String data){
        BNGift gift = null;
        try{
            JSONObject objectGift = new JSONObject(data);
            gift = giftParser.parseBNGift(objectGift);
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return gift;
    }

    private void parseNotification(String data){
        //TODO parsear notifications
    }
}