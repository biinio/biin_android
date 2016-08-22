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
    private BNDataManager dataManager;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.e(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.e(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.e(TAG, "FCM Data Message: " + remoteMessage.getData());
//        {data={"giftIdentifier":"e2f5170e-8d27-4a6e-967a-578e546fe3eb","type":"giftdelivered"}}
        String type = parseData(remoteMessage.getData().toString());
        if(type.equals("giftassigned")) { // determinar si es un gift
            BNGift gift = parseGift(remoteMessage.getData().toString());
            if (gift != null) {
                if(dataManager == null) {
                    dataManager = BNAppManager.getInstance().getDataManagerInstance();
                }
                if (dataManager.addBNGift(gift)) {
                    dataManager.incrementGiftsBadge(getApplicationContext());
                    // llamar al broadcast para avisar a la pantalla principal
                    if(localBroadcastManager == null) {
                        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                    }
                    Intent intent = new Intent("MESSAGING_SERVICE");
                    intent.putExtra("TYPE", "GIFT");
                    intent.putExtra("POSITION", dataManager.getBNGifts().size() - 1);
                    localBroadcastManager.sendBroadcast(intent);
                }
            } else {
                Log.e(TAG, "Error: no Ne pudo parsear el gift (" + remoteMessage.getData() + ")");
            }
        }else{
            if(type.equals("giftdelivered")){ // determinar si es una notificacion de entrega de gift
                if(localBroadcastManager == null) {
                    localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                }
                String giftIdentifier = "";
                try{
                    JSONObject objectNotification = new JSONObject(remoteMessage.getData().toString());
                    JSONObject objectData = objectNotification.getJSONObject("data");
                    giftIdentifier = objectData.getString("giftIdentifier");
                }catch (JSONException e){
                    Log.e(TAG, "Error parseando el JSON.", e);
                }
                Intent intent = new Intent("MESSAGING_SERVICE");
                intent.putExtra("TYPE", "DELIEVRED");
                intent.putExtra("IDENTIFIER", giftIdentifier);
                localBroadcastManager.sendBroadcast(intent);
            }else {
                // sino, es una notificacion solamente
                if (localBroadcastManager == null) {
                    localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                }
                Intent intent = new Intent("MESSAGING_SERVICE");
                intent.putExtra("TYPE", "NOTIFICATION");
                localBroadcastManager.sendBroadcast(intent);
            }
        }
    }

    private String parseData(String data){
        String type = "";
        try{
            JSONObject objectNotification = new JSONObject(data);
            JSONObject objectData = objectNotification.getJSONObject("data");
            type = objectData.getString("type");
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return type;
    }

    private BNGift parseGift(String data){
        BNGift gift = null;
        try{
            JSONObject objectNotification = new JSONObject(data);
            JSONObject objectData = objectNotification.getJSONObject("data");
            JSONObject objectGift = objectData.getJSONObject("gift");
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