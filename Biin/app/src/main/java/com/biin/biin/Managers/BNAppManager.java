package com.biin.biin.Managers;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNAppManager {

    private static BNAppManager ourInstance = new BNAppManager();
    private static BNDataManager dataManager;
    private static BNAnalyticsManager analyticsManager;
    private static BNNetworkManager networkManager;
    private static BNLocationManager positionManager;

    public static BNAppManager getInstance() {
        return ourInstance;
    }

    public BNDataManager getDataManagerInstance() {
        return dataManager;
    }

    public BNAnalyticsManager getAnalyticsManagerInstance() {
        return analyticsManager;
    }

    public BNNetworkManager getNetworkManagerInstance() {
        return networkManager;
    }

    public BNLocationManager getPositionManagerInstance() {
        return positionManager;
    }

    private BNAppManager() {
        // instanciar los otros managers
        dataManager = BNDataManager.getInstance();
        analyticsManager = BNAnalyticsManager.getInstance();
        networkManager = BNNetworkManager.getInstance();
        positionManager = BNLocationManager.getInstance();
    }

}
