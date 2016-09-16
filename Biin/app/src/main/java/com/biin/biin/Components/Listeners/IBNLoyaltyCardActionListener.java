package com.biin.biin.Components.Listeners;

/**
 * Created by ramirezallan on 9/13/16.
 */
public interface IBNLoyaltyCardActionListener {
    void onCardClick(String identifier, int position);
    void onCardEnrolled(String identifier, int position, String name);
    void onCardDeleted(String identifier, int position);
}
