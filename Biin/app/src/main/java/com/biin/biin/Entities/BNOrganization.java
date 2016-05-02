package com.biin.biin.Entities;

import android.graphics.Color;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNOrganization {

    private String identifier;

    //Details
    private String title;
    private String subTitle;

    private String name;
    private String brand;
    private String organizationDescription;
    private String extraInfo;

    private boolean isLoyaltyEnabled;
    //private BNLoyalty loyalty;

    private Color primaryColor;
    private Color secondaryColor;

    //private ArrayList<BNMedia> media = new ArrayList<>();

    private boolean hasNPS;

    public BNOrganization() {
    }

    public BNOrganization(String identifier) {
        this.identifier = identifier;
    }
}
