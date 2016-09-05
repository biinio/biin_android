package com.biin.biin.Entities;

/**
 * Created by ramirezallan on 9/5/16.
 */
public class BNFriend {

    private static final String TAG = "BNFriend";

    private String facebook_id;
    private String facebookName;
    private String facebookAvatarUrl;

    public BNFriend() {
    }

    public BNFriend(String facebook_id, String facebookAvatarUrl, String facebookName) {

        this.facebook_id = facebook_id;
        this.facebookAvatarUrl = facebookAvatarUrl;
        this.facebookName = facebookName;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getFacebookAvatarUrl() {
        return facebookAvatarUrl;
    }

    public void setFacebookAvatarUrl(String facebookAvatarUrl) {
        this.facebookAvatarUrl = facebookAvatarUrl;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }
}
