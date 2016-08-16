package com.biin.biin;

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

        BNGift gift = parseGift(remoteMessage.getData().toString());
        if(gift != null){
            BNDataManager dataManager = BNAppManager.getInstance().getDataManagerInstance();
            if(dataManager.addBNGift(gift)) {
                dataManager.incrementGiftsBadge();
                //TODO llamar al broadcast para avisar a la pantalla principal
            }
        }else{
            Log.e(TAG, "Error: no Ne pudo parsear el gift (" + remoteMessage.getData() + ")");
        }
    }

    private BNGift parseGift(String s){
        BNGift gift = null;
        try{
            JSONObject objectGift = new JSONObject(s);
            gift = giftParser.parseBNGift(objectGift);
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return gift;
    }
}