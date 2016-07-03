package com.biin.biin.Entities;

import android.graphics.Color;
import android.util.Log;

import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Utils.BNUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNSite {

    private static final String TAG = "BNSite";

    private String _id;
    private String identifier;
    private String organizationIdentifier;
    private BNOrganization organization;
    //private NSUUID proximityUUID;
    private int major;

    //Details
    private String title;
    private String subTitle;

    //Color
    private Color titleColor;
    private float stars;

    //Location
    private String country;
    private String state;
    private String city;
    private String zipCode;
    private String streetAddress1;
    private String streetAddress2;
    //private String ubication;
    private String phoneNumber;
    private String email;
    private String nutshell;
    //Gallery
    private List<BNMedia> media = new ArrayList<>();
//    private String images:Array<UIImageView> = Array<UIImageView>()

    //Biins
    //private ArrayList<BNBiin> biins = new ArrayList<>();
    private boolean useWhiteText;
    //Loyalty
    //private String loyalty:BNLoyalty?

    //Social interaction
    //private String biinedCount:Int = 0   //How many time users have biined this element.
    private int collectCount;   //How many time users have collect this site.
    private int commentedCount;    //How many time users have commented this element.

    //private String userBiined = false
    private boolean userCommented;

    private boolean userShared;
    private boolean userFollowed;
    private boolean userCollected;
    private boolean userLiked;

    private Date likeDate;

    private float latitude;
    private float longitude;
    private float biinieProximity;

    private boolean isUserInside;

    //Neighbors are set by geo distance on backend.
    private List<String> neighbors;

    //    private String showcases:Array<String>?
    private List<String> showcases;

    private boolean showInView = true;

    private String siteSchedule;

    public BNSite() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }

    public BNSite(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOrganizationIdentifier() {
        return organizationIdentifier;
    }

    public void setOrganizationIdentifier(String organizationIdentifier) {
        this.organizationIdentifier = organizationIdentifier;
    }

    public BNOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(BNOrganization organization) {
        this.organization = organization;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
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

    public Color getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNutshell() {
        return nutshell;
    }

    public void setNutshell(String nutshell) {
        this.nutshell = nutshell;
    }

    public List<BNMedia> getMedia() {
        return media;
    }

    public void setMedia(List<BNMedia> media) {
        this.media = media;
    }

    public boolean isUseWhiteText() {
        return useWhiteText;
    }

    public void setUseWhiteText(boolean useWhiteText) {
        this.useWhiteText = useWhiteText;
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

    public boolean isUserShared() {
        return userShared;
    }

    public void setUserShared(boolean userShared) {
        this.userShared = userShared;
    }

    public boolean isUserFollowed() {
        return userFollowed;
    }

    public void setUserFollowed(boolean userFollowed) {
        this.userFollowed = userFollowed;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getBiinieProximity() {
        return biinieProximity;
    }

    public void setBiinieProximity(float biinieProximity) {
        this.biinieProximity = biinieProximity;
    }

    public boolean isUserInside() {
        return isUserInside;
    }

    public void setUserInside(boolean userInside) {
        isUserInside = userInside;
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<String> neighbors) {
        this.neighbors = neighbors;
    }

    public List<String> getShowcases() {
        return showcases;
    }

    public void setShowcases(List<String> showcases) {
        this.showcases = showcases;
    }

    public boolean isShowInView() {
        return showInView;
    }

    public void setShowInView(boolean showInView) {
        this.showInView = showInView;
    }

    public String getSiteSchedule() {
        return siteSchedule;
    }

    public void setSiteSchedule(String siteSchedule) {
        this.siteSchedule = siteSchedule;
    }

    public void setShowcasesSite(){
        for (String identifier : this.showcases) {
            BNShowcase showcase = BNAppManager.getDataManagerInstance().getBNShowcase(identifier);
            if(showcase != null){
                showcase.setSite(this);
            }
        }
    }


    public JSONObject getModel(){
        JSONObject model = new JSONObject();
        try {
            model.put("type", "site");
            model.put("identifier", identifier);
        }catch (JSONException e){
            Log.e(TAG, "Error: " + e.getMessage());
        }
        return model;
    }


}
