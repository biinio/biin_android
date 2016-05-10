package com.biin.biin.Entities;

/**
 * Created by ramirezallan on 5/9/16.
 */
public class BNMedia {
    
    private BNMediaType mediaType;
    private String url;
    private int vibrantColor;
    private int vibrantDarkColor;
    private int vibrantLightColor;

    public BNMedia() {
    }

    public BNMedia(BNMediaType mediaType, String url) {
        this.mediaType = mediaType;
        this.url = url;
    }

    public BNMedia(BNMediaType mediaType, String url, int vibrantColor, int vibrantDarkColor, int vibrantLightColor) {
        this.mediaType = mediaType;
        this.url = url;
        this.vibrantColor = vibrantColor;
        this.vibrantDarkColor = vibrantDarkColor;
        this.vibrantLightColor = vibrantLightColor;
    }

    public BNMediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(BNMediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVibrantColor() {
        return vibrantColor;
    }

    public void setVibrantColor(int vibrantColor) {
        this.vibrantColor = vibrantColor;
    }

    public int getVibrantDarkColor() {
        return vibrantDarkColor;
    }

    public void setVibrantDarkColor(int vibrantDarkColor) {
        this.vibrantDarkColor = vibrantDarkColor;
    }

    public int getVibrantLightColor() {
        return vibrantLightColor;
    }

    public void setVibrantLightColor(int vibrantLightColor) {
        this.vibrantLightColor = vibrantLightColor;
    }

    enum BNMediaType {
        Image,
        Video
    }

}
