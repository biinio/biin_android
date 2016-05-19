package com.biin.biin.Entities;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNHighlight {

    private String identifier;
    private String showcaseIdentifier;
    private String siteIdentifier;

    public BNHighlight() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getShowcaseIdentifier() {
        return showcaseIdentifier;
    }

    public void setShowcaseIdentifier(String showcaseIdentifier) {
        this.showcaseIdentifier = showcaseIdentifier;
    }

    public String getSiteIdentifier() {
        return siteIdentifier;
    }

    public void setSiteIdentifier(String siteIdentifier) {
        this.siteIdentifier = siteIdentifier;
    }
}
