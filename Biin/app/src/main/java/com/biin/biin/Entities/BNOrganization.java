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

    private int primaryColor;
    private int secondaryColor;

    //private ArrayList<BNMedia> media = new ArrayList<>();

    private boolean hasNPS;

    public BNOrganization() {
    }

    public BNOrganization(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOrganizationDescription() {
        return organizationDescription;
    }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isLoyaltyEnabled() {
        return isLoyaltyEnabled;
    }

    public void setLoyaltyEnabled(boolean loyaltyEnabled) {
        isLoyaltyEnabled = loyaltyEnabled;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public boolean isHasNPS() {
        return hasNPS;
    }

    public void setHasNPS(boolean hasNPS) {
        this.hasNPS = hasNPS;
    }
}
