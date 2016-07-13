package com.biin.biin.Entities;

import android.util.Log;

import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ramirezallan on 7/8/16.
 */
public class BiinieAction {

    private static final String TAG = "BiinieAction";

    private Date at;
    private int did;
    private String to;
    private String by;

    public BiinieAction(String by, int did, String to) {
        this.at = Calendar.getInstance().getTime();
        this.by = by;
        this.did = did;
        this.to = to;
    }

    public JSONObject getModel() {
        JSONObject model = new JSONObject();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(BNUtils.getActionDateFormat());
            model.put("at", formatter.format(at));
            model.put("did", String.valueOf(did));
            model.put("by", by);
            model.put("to", to);
            model.put("whom", BNAppManager.getInstance().getDataManagerInstance().getBiinie().getIdentifier());
        } catch (JSONException e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
        return model;
    }

    public static final String AndroidApp = "biin_android";

    public static final int NONE = 0;

    public static final int ENTER_BIIN_REGION = 1;
    public static final int EXIT_BIIN_REGION = 2;
    public static final int BIIN_NOTIFIED = 3;
    public static final int NOTIFICATION_OPENED = 4;

    public static final int ENTER_ELEMENT_VIEW = 5;
    public static final int EXIT_ELEMENT_VIEW = 6;
    public static final int LIKE_ELEMENT = 7;
    public static final int UNLIKE_ELEMENT = 8;
    public static final int COLLECTED_ELEMENT = 9;
    public static final int UNCOLLECTED_ELEMENT = 10;
    public static final int SHARE_ELEMENT = 11;

    public static final int ENTER_SITE_VIEW = 12;
    public static final int EXIT_SITE_VIEW = 13;
    public static final int LIKE_SITE = 14;
    public static final int UNLIKE_SITE = 15;
    public static final int FOLLOW_SITE = 16;
    public static final int UNFOLLOW_SITE = 17;
    public static final int SHARE_SITE = 18;

    public static final int ENTER_BIIN = 19;
    public static final int EXIT_BIIN = 20;

    public static final int OPEN_APP = 21;
    public static final int CLOSE_APP = 22;

    public static final int BEACON_BATTERY = 23;
}
