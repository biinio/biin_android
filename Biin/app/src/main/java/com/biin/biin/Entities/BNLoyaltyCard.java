package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ramirezallan on 8/23/16.
 */
public class BNLoyaltyCard {

    private String identifier;
    private String title;
    private String rule;
    private String goal;
    private String conditions;

    private String elementIdentifier;

    private boolean isCompleted = false;
    private boolean isBiinieEnrolled = false;
    private boolean isUnavailable = false;
    private Date startDate;
    private Date endDate;

    private List<BNLoyaltyCard_Slot> slots;

    private boolean isFull = false;

    public BNLoyaltyCard() {
        slots = new ArrayList<>();
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getElementIdentifier() {
        return elementIdentifier;
    }

    public void setElementIdentifier(String elementIdentifier) {
        this.elementIdentifier = elementIdentifier;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isBiinieEnrolled() {
        return isBiinieEnrolled;
    }

    public void setBiinieEnrolled(boolean biinieEnrolled) {
        isBiinieEnrolled = biinieEnrolled;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isUnavailable() {
        return isUnavailable;
    }

    public void setUnavailable(boolean unavailable) {
        isUnavailable = unavailable;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<BNLoyaltyCard_Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<BNLoyaltyCard_Slot> slots) {
        this.slots = slots;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addStar(){

    }

    private class BNLoyaltyCard_Slot {
        private boolean isFilled = false;
    }
}
