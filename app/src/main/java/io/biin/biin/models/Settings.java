package io.biin.biin.models;

/**
 * Created by Ivan on 1/23/16.
 */
public class Settings {
    private static Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    public static String DEV_URL_SERVER = "";
    public static String QA_URL_SERVER = "";
    public static String DEMO_URL_SERVER = "";
    public static String PRODUCTION_URL_SERVER = "";

    private Settings() {
    }
}
