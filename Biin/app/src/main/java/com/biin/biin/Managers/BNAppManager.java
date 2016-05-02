package com.biin.biin.Managers;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNAppManager {

    private static BNAppManager ourInstance = new BNAppManager();

    public static BNAppManager getInstance() {
        return ourInstance;
    }

    private BNAppManager() {
        // TODO instanciar los otros managers
    }

}
