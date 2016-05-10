package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.List;

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
    private String description;
    private String extraInfo;

    private boolean isLoyaltyEnabled;
    private boolean isUsingBrandColors;
    //private BNLoyalty loyalty;

    private int primaryColor;
    private int secondaryColor;

    private List<BNMedia> media = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isUsingBrandColors() {
        return isUsingBrandColors;
    }

    public void setUsingBrandColors(boolean usingBrandColors) {
        isUsingBrandColors = usingBrandColors;
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

    public List<BNMedia> getMedia() {
        return media;
    }

    public void setMedia(List<BNMedia> media) {
        this.media = media;
    }

    public boolean isHasNPS() {
        return hasNPS;
    }

    public void setHasNPS(boolean hasNPS) {
        this.hasNPS = hasNPS;
    }
}
