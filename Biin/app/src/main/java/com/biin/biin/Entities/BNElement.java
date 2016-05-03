package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public String getNutshellDescriptionTitle() {
        return nutshellDescriptionTitle;
    }

    public void setNutshellDescriptionTitle(String nutshellDescriptionTitle) {
        this.nutshellDescriptionTitle = nutshellDescriptionTitle;
    }

    public String getNutshellDescription() {
        return nutshellDescription;
    }

    public void setNutshellDescription(String nutshellDescription) {
        this.nutshellDescription = nutshellDescription;
    }

    public boolean isTaxIncludedInPrice() {
        return isTaxIncludedInPrice;
    }

    public void setTaxIncludedInPrice(boolean taxIncludedInPrice) {
        isTaxIncludedInPrice = taxIncludedInPrice;
    }

    public boolean isHasFromPrice() {
        return hasFromPrice;
    }

    public void setHasFromPrice(boolean hasFromPrice) {
        this.hasFromPrice = hasFromPrice;
    }

    public String getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(String fromPrice) {
        this.fromPrice = fromPrice;
    }

    public boolean isHasListPrice() {
        return hasListPrice;
    }

    public void setHasListPrice(boolean hasListPrice) {
        this.hasListPrice = hasListPrice;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isHasPrice() {
        return hasPrice;
    }

    public void setHasPrice(boolean hasPrice) {
        this.hasPrice = hasPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isHasSaving() {
        return hasSaving;
    }

    public void setHasSaving(boolean hasSaving) {
        this.hasSaving = hasSaving;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isHasTimming() {
        return hasTimming;
    }

    public void setHasTimming(boolean hasTimming) {
        this.hasTimming = hasTimming;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isHasQuantity() {
        return hasQuantity;
    }

    public void setHasQuantity(boolean hasQuantity) {
        this.hasQuantity = hasQuantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(String reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getClaimedQuantity() {
        return claimedQuantity;
    }

    public void setClaimedQuantity(String claimedQuantity) {
        this.claimedQuantity = claimedQuantity;
    }

    public String getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(String actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public boolean isUseWhiteText() {
        return useWhiteText;
    }

    public void setUseWhiteText(boolean useWhiteText) {
        this.useWhiteText = useWhiteText;
    }

    public boolean isActivateNotification() {
        return activateNotification;
    }

    public void setActivateNotification(boolean activateNotification) {
        this.activateNotification = activateNotification;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentedCount() {
        return commentedCount;
    }

    public void setCommentedCount(int commentedCount) {
        this.commentedCount = commentedCount;
    }

    public boolean isUserCommented() {
        return userCommented;
    }

    public void setUserCommented(boolean userCommented) {
        this.userCommented = userCommented;
    }

    public boolean isUserViewed() {
        return userViewed;
    }

    public void setUserViewed(boolean userViewed) {
        this.userViewed = userViewed;
    }

    public boolean isUserShared() {
        return userShared;
    }

    public void setUserShared(boolean userShared) {
        this.userShared = userShared;
    }

    public boolean isUserCollected() {
        return userCollected;
    }

    public void setUserCollected(boolean userCollected) {
        this.userCollected = userCollected;
    }

    public boolean isUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public boolean isDownloadCompleted() {
        return isDownloadCompleted;
    }

    public void setDownloadCompleted(boolean downloadCompleted) {
        isDownloadCompleted = downloadCompleted;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }

    public ArrayList<String> getCategoriesIdentifiers() {
        return categoriesIdentifiers;
    }

    public void setCategoriesIdentifiers(ArrayList<String> categoriesIdentifiers) {
        this.categoriesIdentifiers = categoriesIdentifiers;
    }

    public BNShowcase getShowcase() {
        return showcase;
    }

    public void setShowcase(BNShowcase showcase) {
        this.showcase = showcase;
    }

    public String getDetailsHtml() {
        return detailsHtml;
    }

    public void setDetailsHtml(String detailsHtml) {
        this.detailsHtml = detailsHtml;
    }

    public boolean isHasCallToAction() {
        return hasCallToAction;
    }

    public void setHasCallToAction(boolean hasCallToAction) {
        this.hasCallToAction = hasCallToAction;
    }

    public String getCallToActionURL() {
        return callToActionURL;
    }

    public void setCallToActionURL(String callToActionURL) {
        this.callToActionURL = callToActionURL;
    }

    public String getCallToActionTitle() {
        return callToActionTitle;
    }

    public void setCallToActionTitle(String callToActionTitle) {
        this.callToActionTitle = callToActionTitle;
    }

    public boolean isRemovedFromShowcase() {
        return isRemovedFromShowcase;
    }

    public void setRemovedFromShowcase(boolean removedFromShowcase) {
        isRemovedFromShowcase = removedFromShowcase;
    }

    public class BNElementLite {
        private String _id;
        private String identifier;
        private boolean isReady;
        private boolean isHighlight;
        private HashMap<String,BNCategory.BNCategoryLite> categories;

        public BNElementLite() {
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

        public boolean isReady() {
            return isReady;
        }

        public void setReady(boolean ready) {
            isReady = ready;
        }

        public boolean isHighlight() {
            return isHighlight;
        }

        public void setHighlight(boolean highlight) {
            isHighlight = highlight;
        }

        public HashMap<String, BNCategory.BNCategoryLite> getCategories() {
            return categories;
        }

        public void setCategories(HashMap<String, BNCategory.BNCategoryLite> categories) {
            this.categories = categories;
        }
    }
}
