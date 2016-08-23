package com.biin.biin.Entities;

/**
 * Created by ramirezallan on 8/23/16.
 */
public class BNLoyalty {

    private String organizationIdentifier;
    private BNLoyaltyCard loyaltyCard;

    public BNLoyaltyCard getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(BNLoyaltyCard loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }

    public String getOrganizationIdentifier() {
        return organizationIdentifier;
    }

    public void setOrganizationIdentifier(String organizationIdentifier) {
        this.organizationIdentifier = organizationIdentifier;
    }

    public void addStar(){
        loyaltyCard.addStar();
    }
}
