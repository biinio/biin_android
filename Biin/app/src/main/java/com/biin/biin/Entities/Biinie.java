package com.biin.biin.Entities;

import android.util.Log;

import com.biin.biin.Utils.BNUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class Biinie {

    private static final String TAG = "Biinie";

    private String facebook_id;
    private String identifier;
    private String biinName;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthDate;
    private String password;
    private String gender;
    private ArrayList<Biinie> friends = new ArrayList<>();

    private int biins;
    private int following;
    private int followers;
    private String jsonUrl;

    private ArrayList<BNCategory> categories = new ArrayList<>();
    //private LinkedHashMap<String, BNCollection> collections;
    private String temporalCollectionIdentifier;
    private String[] BiinieAction;

    private boolean isEmailVerified;

    private int newNotificationCount;
    private int notificationIndex;

    private boolean isInStore;
    private int actionCounter;
    private String[] storedElementsViewed;
    private LinkedHashMap<String, String> elementsViewed;

    private String facebookAvatarUrl;
    private List<BNFriend> facebookFriends;

    public List<BNFriend> getFacebookFriends() {
        return facebookFriends;
    }

    public void setFacebookFriends(List<BNFriend> facebookFriends) {
        this.facebookFriends = facebookFriends;
    }

    public Biinie(String identifier, String firstName, String lastName, String email) {
        this.identifier = identifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.facebookAvatarUrl = "";
    }

    public Biinie(String identifier, String firstName, String lastName, String email, String gender) {
        this(identifier, firstName, lastName, email);
        this.gender = gender;
    }

    public Biinie() {
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBiinName() {
        return biinName;
    }

    public void setBiinName(String biinName) {
        this.biinName = biinName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<Biinie> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Biinie> friends) {
        this.friends = friends;
    }

    public int getBiins() {
        return biins;
    }

    public void setBiins(int biins) {
        this.biins = biins;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public ArrayList<BNCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<BNCategory> categories) {
        this.categories = categories;
    }

    public String getTemporalCollectionIdentifier() {
        return temporalCollectionIdentifier;
    }

    public void setTemporalCollectionIdentifier(String temporalCollectionIdentifier) {
        this.temporalCollectionIdentifier = temporalCollectionIdentifier;
    }

    public String[] getBiinieAction() {
        return BiinieAction;
    }

    public void setBiinieAction(String[] biinieAction) {
        BiinieAction = biinieAction;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public int getNewNotificationCount() {
        return newNotificationCount;
    }

    public void setNewNotificationCount(int newNotificationCount) {
        this.newNotificationCount = newNotificationCount;
    }

    public int getNotificationIndex() {
        return notificationIndex;
    }

    public void setNotificationIndex(int notificationIndex) {
        this.notificationIndex = notificationIndex;
    }

    public boolean isInStore() {
        return isInStore;
    }

    public void setInStore(boolean inStore) {
        isInStore = inStore;
    }

    public int getActionCounter() {
        return actionCounter;
    }

    public void setActionCounter(int actionCounter) {
        this.actionCounter = actionCounter;
    }

    public String[] getStoredElementsViewed() {
        return storedElementsViewed;
    }

    public void setStoredElementsViewed(String[] storedElementsViewed) {
        this.storedElementsViewed = storedElementsViewed;
    }

    public LinkedHashMap<String, String> getElementsViewed() {
        return elementsViewed;
    }

    public void setElementsViewed(LinkedHashMap<String, String> elementsViewed) {
        this.elementsViewed = elementsViewed;
    }

    public String getFacebookAvatarUrl() {
        return facebookAvatarUrl;
    }

    public void setFacebookAvatarUrl(String facebookAvatarUrl) {
        this.facebookAvatarUrl = facebookAvatarUrl;
    }

    public JSONObject getModel(){
        SimpleDateFormat apiFormatter = new SimpleDateFormat(BNUtils.getActionDateFormat());
        String date = apiFormatter.format(birthDate);
        JSONObject model = new JSONObject();
        try {
            model.put("gender", gender);
            model.put("facebook_id", facebook_id);
            model.put("lastName", lastName);
//            model.put("password", password);
            model.put("firstName", firstName);
            model.put("email", email);
//            model.put("facebookAvatarUrl", "");
            model.put("isEmailVerified", isEmailVerified ? "1" : "0");
            model.put("birthDate", date);
        }catch (JSONException e){
            Log.e(TAG, "Error: " + e.getMessage());
        }

        return model;
    }

}
