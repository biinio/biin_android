package com.biin.biin.Managers;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNPositionManager {
    private static BNPositionManager ourInstance = new BNPositionManager();

    protected static BNPositionManager getInstance() {
        return ourInstance;
    }

    private BNPositionManager() {
    }
}
