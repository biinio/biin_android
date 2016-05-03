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

    public BNCategory() {
    }

    public BNCategory(String identifier) {
        this.identifier = identifier;
    }

    public BNCategory(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public ArrayList<BNCategorySiteDetails> getSitesDetails() {
        return sitesDetails;
    }

    public void setSitesDetails(ArrayList<BNCategorySiteDetails> sitesDetails) {
        this.sitesDetails = sitesDetails;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public boolean isUserCategory() {
        return isUserCategory;
    }

    public void setUserCategory(boolean userCategory) {
        isUserCategory = userCategory;
    }

    public boolean isHasSites() {
        return hasSites;
    }

    public void setHasSites(boolean hasSites) {
        this.hasSites = hasSites;
    }

    public HashMap<String, BNElement> getElements() {
        return elements;
    }

    public void setElements(HashMap<String, BNElement> elements) {
        this.elements = elements;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
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

    public class BNCategoryLite {
        private String _id;
        private String identifier;
        private String url;
        private String displayName;
        private String name;

        public BNCategoryLite() {
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
