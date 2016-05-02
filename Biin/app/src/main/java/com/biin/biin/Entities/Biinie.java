package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class Biinie {

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
    //private HashMap<String, BNCollection> collections;
    private String temporalCollectionIdentifier;
    private String[] BiinieAction;

    private boolean isEmailVerified;

    private int newNotificationCount;
    private int notificationIndex;

    private boolean isInStore;
    private int actionCounter;
    private String[] storedElementsViewed;
    private HashMap<String, String> elementsViewed;

    private String facebookAvatarUrl;

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

}
