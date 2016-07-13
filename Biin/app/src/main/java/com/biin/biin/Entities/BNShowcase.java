package com.biin.biin.Entities;

import com.biin.biin.Managers.BNAppManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<String> elements = new ArrayList<>();
    private int elements_quantity;
    private int batch = 1;

    //private String siteIdentifier;
    private BNSite site;

    public BNShowcase() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isShowcaseGameCompleted() {
        return isShowcaseGameCompleted;
    }

    public void setShowcaseGameCompleted(boolean showcaseGameCompleted) {
        isShowcaseGameCompleted = showcaseGameCompleted;
    }

    public boolean isRequestPending() {
        return isRequestPending;
    }

    public void setRequestPending(boolean requestPending) {
        isRequestPending = requestPending;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isUserNotified() {
        return isUserNotified;
    }

    public void setUserNotified(boolean userNotified) {
        isUserNotified = userNotified;
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

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public int getElements_quantity() {
        return elements_quantity;
    }

    public void setElements_quantity(int elements_quantity) {
        this.elements_quantity = elements_quantity;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public BNSite getSite() {
        return site;
    }

    public void setSite(BNSite site) {
        this.site = site;
    }

    public void setElementsShowcase(){
        for (String identifier : this.elements) {
            BNElement element = BNAppManager.getInstance().getDataManagerInstance().getBNElement(identifier);
            if(element != null){
                element.setShowcase(this);
            }
        }
    }
}
