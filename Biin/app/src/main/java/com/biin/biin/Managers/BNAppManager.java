package com.biin.biin.Managers;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNAppManager {

    private static BNAppManager ourInstance = new BNAppManager();
    private static BNDataManager dataManager;
    private static BNNetworkManager networkManager;
    private static BNPositionManager positionManager;

    public static BNAppManager getInstance() {
        return ourInstance;
    }

    public static BNDataManager getDataManagerInstance() {
        return dataManager;
    }

    public static BNNetworkManager getNetworkManagerInstance() {
        return networkManager;
    }

    public static BNPositionManager getPositionManagerInstance() {
        return positionManager;
    }

    private BNAppManager() {
        // instanciar los otros managers
        dataManager = BNDataManager.getInstance();
        networkManager = BNNetworkManager.getInstance();
        positionManager = BNPositionManager.getInstance();
    }

}
