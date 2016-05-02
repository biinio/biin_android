package com.biin.biin;

import com.biin.biin.Entities.BNSite;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class AppManager {

    private static AppManager ourInstance = new AppManager();

    private HashMap<String, BNSite> sites = new HashMap<String, BNSite>();

    public static AppManager getInstance() {
        return ourInstance;
    }

    private AppManager() {
    }

    public void setSites(HashMap<String, BNSite> sites) {
        // reemplazar la coleccion cmopleta de sites
        this.sites = sites;
    }

    public int addSites(HashMap<String, BNSite> sites) {
        // TODO agregar sites a la coleccion (solo los que no existian previamente)
        this.sites = sites;
        // TODO retornar el numero de sites agregados a la coleccion
        return 0;
    }

    public boolean addSite(BNSite site) {
        // TODO agregar un site a la coleccion
        this.sites.put("", site);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNSite getBNSite(String identifier) {
        // TODO obtener un site por su identificador
        return null;
    }

    public boolean removeBNSite(String identifier) {
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return true;
    }

    public HashMap<String,BNSite> getSites(){
        // retornar la lista de sites
        return this.sites;
    }
}
