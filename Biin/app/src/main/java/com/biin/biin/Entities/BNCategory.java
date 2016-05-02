package com.biin.biin.Entities;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNCategory {

    public class BNCategorySiteDetails{
        private String identifier;
        private String json;
        private float biinieProximity;
    }

    private String identifier;
    private String name;
    private String json;
    private ArrayList<BNCategorySiteDetails> sitesDetails = new ArrayList<>();
    private boolean isDownloaded;
    private boolean isUserCategory;
    private boolean hasSites;
    private HashMap<String,BNElement> elements;
    private int priority = 1;
    private Color backgroundColor;

    public BNCategory(String identifier) {
        this.identifier = identifier;
    }

    public BNCategory(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    enum BNCategoryType {
        none,
        general,
        personalcare,
        vacations,
        shoes,
        game,
        outdoors,
        health,
        food,
        sports,
        education,
        fashion,
        music,
        movies,
        technology,
        entertaiment,
        travel
    }
}
