package com.biin.biin.Managers;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNNetworkManager {

    private static BNNetworkManager ourInstance = new BNNetworkManager();
    private static boolean production = true;

    private static String URL_BASE = "https://www.biin.io/";
    private static String URL_BASE_DEV = "https://www.biin.io/"; // TODO usar el server de pruebas

    private static String URL_GET_USER = "";
    private static String URL_GET_INIT = "mobile/initialData/";

    private static String URL_GET_INIT_TEST = URL_GET_INIT + "25e854b6-b5ee-483b-80fd-7aa4c85eb386/9.73854872449546/-83.9987999326416";

    public static BNNetworkManager getInstance() {
        return ourInstance;
    }

    private BNNetworkManager() {
    }

    public String getUrlBase() {
        if(production){
            return URL_BASE;
        }else{
            return URL_BASE_DEV;
        }
    }

    public String getUrlGetUser() {
        return getUrlBase() + URL_GET_USER;
    }

    public String getUrlGetInit() {
        return getUrlBase() + URL_GET_INIT;
    }

    public String getUrlGetInitTest() {
        return getUrlBase() + URL_GET_INIT_TEST;
    }


}
