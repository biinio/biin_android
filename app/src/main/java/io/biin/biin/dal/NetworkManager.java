package io.biin.biin.dal;

import com.octo.android.robospice.SpiceManager;

import io.biin.biin.dal.service.testservice;

/**
 * Created by Ivan on 1/22/16.
 */
public class NetworkManager {

    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private SpiceManager spiceManager;

    private NetworkManager() {
        spiceManager = new SpiceManager(testservice.class);
    }

    public static enum HttpMethod{
        GET,
        POST,
        PUT
    }

    public void GetRequest(String pURL){
        sendRequest(HttpMethod.GET, pURL);
    }

    public void PostRequest(String pURL){
        sendRequest(HttpMethod.POST, pURL);
    }

    public void PutRequest(String pURL){
        sendRequest(HttpMethod.PUT, pURL);
    }

    private void sendRequest(HttpMethod pMethodType , String pURL){
        //sendRequest(pMethodType,pURL);
    }
}
