package com.biin.biin.Entities;

/**
 * Created by ramirezallan on 7/29/16.
 */
public class BNBeacon {

    private String identfier;
    private String UUID;
    private int minor;
    private int major;

    public BNBeacon() {
    }

    public BNBeacon(String identfier, int major, int minor, String UUID) {
        this.identfier = identfier;
        this.major = major;
        this.minor = minor;
        this.UUID = UUID;
    }

    public String getIdentfier() {
        return identfier;
    }

    public void setIdentfier(String identfier) {
        this.identfier = identfier;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }
}
