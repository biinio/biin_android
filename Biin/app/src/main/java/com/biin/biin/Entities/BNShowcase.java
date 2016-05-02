package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNShowcase {

    private String _id;
    private String identifier;
    private boolean isShowcaseGameCompleted;
    private boolean isRequestPending = true;
    private Date lastUpdate;

    //Context private Stringiables
    private boolean isDefault;
    private boolean isUserNotified;

    //Details
    private String title;
    private String subTitle;

    //Elements
    private ArrayList<BNElement> elements = new ArrayList<>();
    private int elements_quantity;
    private int batch = 1;

    //private String siteIdentifier;
    private BNSite site;

    public BNShowcase() {
    }
}
