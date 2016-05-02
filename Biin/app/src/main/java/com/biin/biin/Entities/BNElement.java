package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNElement {

    private String _id;
    private String identifier;
    private int position;
    private String title;
    private String subTitle;
    private String nutshellDescriptionTitle;
    private String nutshellDescription;

    private boolean isTaxIncludedInPrice;
    private boolean hasFromPrice;
    private String fromPrice;
    private boolean hasListPrice;
    private String listPrice;
    private boolean hasDiscount;
    private String discount;
    private boolean hasPrice; //after discount applied
    private String price; //after discount applied
    private boolean hasSaving;
    private String savings;
    private String currency;

    private boolean hasTimming;
    private Date initialDate;
    private Date expirationDate;

    private boolean hasQuantity;
    private String quantity;
    private String reservedQuantity;
    private String claimedQuantity;
    private String actualQuantity;
    private float stars;
    private boolean useWhiteText;

    //private ArrayList<BNMedia> media = new ArrayList<>();
    //private ArrayList<UIImageView> gallery = new ArrayList<>();

    private boolean activateNotification;
    //private ArrayList<BNNotification> notifications;

    private int collectCount;   //How many time users have collect this element.
    private int commentedCount;    //How many time users have commented this element.

    private boolean userCommented;
    private boolean userViewed;
    private boolean userShared;
    private boolean userCollected;
    private boolean userLiked;

    private boolean isDownloadCompleted;
    private boolean isHighlight;
    private ArrayList<String> categoriesIdentifiers;
    private BNShowcase showcase;

    private String detailsHtml;

    private boolean hasCallToAction;
    private String callToActionURL;
    private String callToActionTitle;
    private boolean isRemovedFromShowcase;

    public BNElement() {
    }
}
