package com.biin.biin.Entities;

import java.util.Date;

/**
 * Created by ramirezallan on 9/1/16.
 */
public class BNNotification {
    private String identifier;
    private String title;
    private String message;
    private Date receivedDate;

    public BNNotification() {
    }

    public BNNotification(String identifier, String message, String title, Date receivedDate) {
        this.identifier = identifier;
        this.message = message;
        this.title = title;
        this.receivedDate = receivedDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }
}
