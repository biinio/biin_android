package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ramirezallan on 8/4/16.
 */
public class BNGift {
    private String identifier;
    private String elementIdentifier;
    private String organizationIdentifier;
    private BNElement element;
    private BNOrganization organization;

    private String name;
    private String message;

    private boolean hasExpirationDate = false;
    private Date expirationDate;
    private Date receivedDate;

    private List<String> sites;

    private int status;
    private List<BNMedia> media;

    private int primaryColor;
    private int secondaryColor;

    public BNGift() {
        sites = new ArrayList<>();
        media = new ArrayList<>();
    }

    public BNElement getElement() {
        return element;
    }

    public void setElement(BNElement element) {
        this.element = element;
    }

    public String getElementIdentifier() {
        return elementIdentifier;
    }

    public void setElementIdentifier(String elementIdentifier) {
        this.elementIdentifier = elementIdentifier;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isHasExpirationDate() {
        return hasExpirationDate;
    }

    public void setHasExpirationDate(boolean hasExpirationDate) {
        this.hasExpirationDate = hasExpirationDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<BNMedia> getMedia() {
        return media;
    }

    public void setMedia(List<BNMedia> media) {
        this.media = media;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BNOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(BNOrganization organization) {
        this.organization = organization;
    }

    public String getOrganizationIdentifier() {
        return organizationIdentifier;
    }

    public void setOrganizationIdentifier(String organizationIdentifier) {
        this.organizationIdentifier = organizationIdentifier;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public abstract class BNGiftStatus {
        public static final int NONE = 0;
        public static final int SENT = 1;
        public static final int REFUSED = 2;
        public static final int SHARED = 3;
        public static final int CLAIMED = 4;
        public static final int DELIVERED = 5;
    }
}
