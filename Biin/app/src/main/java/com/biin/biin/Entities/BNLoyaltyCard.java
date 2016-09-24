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
    private Date enrolledDate;

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

    public void setSlots(int slots) {
        for (int i = 0; i < slots; i++) {
            this.slots.add(new BNLoyaltyCard_Slot(false));
        }
    }

    public void setSlotsFilled(int slots) {
        if(slots > 0) {
            for (int i = 0; i < slots; i++) {
                this.slots.get(i).setFilled(true);
            }
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
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
        boolean isCompleted = false;
        if(slots.size() > 0) {
            boolean filled = false;
            int i = 0;
            while (i < slots.size() && !filled) {
                if(!this.slots.get(i).isFilled()){
                    this.slots.get(i).setFilled(true);
                    filled = true;
                }
                i++;
            }
            if(!filled || i == slots.size()){
                isCompleted = true;
            }
        }
    }

    public class BNLoyaltyCard_Slot {

        private boolean isFilled = false;

        public BNLoyaltyCard_Slot(boolean isFilled) {
            this.isFilled = isFilled;
        }

        public boolean isFilled() {
            return isFilled;
        }

        public void setFilled(boolean filled) {
            isFilled = filled;
        }
    }
}
