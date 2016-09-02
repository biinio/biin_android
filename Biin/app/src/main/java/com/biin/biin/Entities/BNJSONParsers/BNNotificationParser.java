package com.biin.biin.Entities.BNJSONParsers;

import android.util.Log;

import com.biin.biin.Entities.BNNotification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by ramirezallan on 9/2/16.
 */
public class BNNotificationParser {

    private static final String TAG = "BNNotificationParser";

    public BNNotification parseBNNotification(JSONObject objectNotification){
        BNNotification notification = new BNNotification();
        try{
//            notification.setIdentifier(objectNotification.getString("identifier"));
            notification.setTitle(objectNotification.getString("title"));
            notification.setMessage(objectNotification.getString("message"));
            Date received = new Date();
            received.setTime(Calendar.getInstance().getTimeInMillis());
            notification.setReceivedDate(received);
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return notification;
    }

    public LinkedHashMap<String, BNNotification> parseBNNotifications(JSONArray arrayNotifications){
        LinkedHashMap<String, BNNotification> result = new LinkedHashMap<>();
        try{
            for(int i = 0; i < arrayNotifications.length(); i++){
                JSONObject objectNotification = (JSONObject) arrayNotifications.get(i);
                BNNotification notification = parseBNNotification(objectNotification);

                result.put(notification.getIdentifier(), notification);
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return result;
    }

}
