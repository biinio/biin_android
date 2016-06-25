package com.biin.biin.Managers;

import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNDataManager {

    private static BNDataManager ourInstance = new BNDataManager();

    private Biinie biinie = new Biinie();

    private LinkedHashMap<String, BNSite> sites = new LinkedHashMap<>();
    private LinkedHashMap<String, BNSite> nearBySites = new LinkedHashMap<>();
    private LinkedHashMap<String, BNSite> favouriteSites = new LinkedHashMap<>();
    private LinkedHashMap<String, BNShowcase> showcases = new LinkedHashMap<>();
    private LinkedHashMap<String, BNOrganization> organizations = new LinkedHashMap<>();
    private LinkedHashMap<String, BNElement> elements = new LinkedHashMap<>();
    private LinkedHashMap<String, BNElement> elements_by_id = new LinkedHashMap<>();
    private LinkedHashMap<String, BNCategory> categories = new LinkedHashMap<>();
    private LinkedHashMap<String, BNElement> favouriteElements = new LinkedHashMap<>();
    private List<BNHighlight> highlights = new ArrayList<>();

    private LinkedHashMap<String, BNSite> likedSites = new LinkedHashMap<>();
    private LinkedHashMap<String, BNSite> unlikedSites = new LinkedHashMap<>();

    private LinkedHashMap<String, BNSite> pendingLikeSites = new LinkedHashMap<>();
    private LinkedHashMap<String, BNSite> pendingUnlikeSites = new LinkedHashMap<>();

    protected static BNDataManager getInstance() {
        return ourInstance;
    }

    private BNDataManager() {
    }

    /****************** Biinie start ******************/

    public void setBiinie(Biinie biinie) {
        // reemplazar el biinie
        this.biinie = biinie;
    }

    public Biinie getBiinie() {
        // obtener el biinie
        return this.biinie;
    }

    /****************** Biinie end ******************/


    /****************** Sites start ******************/

    public void setBNSites(LinkedHashMap<String, BNSite> sites) {
        // reemplazar la coleccion completa de sites
        this.sites = sites;
    }

    public int addBNSites(LinkedHashMap<String, BNSite> sites) {
        // TODO agregar sites a la coleccion (solo los que no existian previamente)
        //this.sites = sites;
        // TODO retornar el numero de sites agregados a la coleccion
        return 0;
    }

    public boolean addBNSite(BNSite site) {
        // TODO agregar un site a la coleccion
        this.sites.put(site.getIdentifier(), site);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNSite getBNSite(String identifier) {
        // obtener un site por su identificador
        return this.sites.get(identifier);
    }

    public boolean removeBNSite(String identifier) {
        // TODO remover un site de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public LinkedHashMap<String,BNSite> getBNSites(){
        // retornar la lista de sites
        return this.sites;
    }

    /****************** Sites end ******************/


    /****************** Sites near by start ******************/

    public void setNearByBNSites(LinkedHashMap<String, BNSite> sites) {
        // reemplazar la coleccion completa de sites
        this.nearBySites = sites;
    }

    public int addNearByBNSites(LinkedHashMap<String, BNSite> sites) {
        // TODO agregar sites a la coleccion (solo los que no existian previamente)
        this.nearBySites = sites;
        // TODO retornar el numero de sites agregados a la coleccion
        return 0;
    }

    public boolean addNearByBNSite(BNSite site) {
        boolean added = false;
        // agregar un site a la coleccion
        if(!this.nearBySites.containsKey(site.getIdentifier())){
            this.nearBySites.put(site.getIdentifier(), site);
            added = true;
        }
        // retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return added;
    }

    public BNSite getNearByBNSite(String identifier) {
        // TODO obtener un site por su identificador
        return null;
    }

    public boolean removeNearByBNSite(String identifier) {
        boolean removed = false;
        // remover un site de la coleccion
        if(!this.nearBySites.containsKey(identifier)){
            this.nearBySites.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return removed;
    }

    public LinkedHashMap<String,BNSite> getNearByBNSites(boolean favorites){
        LinkedHashMap<String,BNSite> sites = this.nearBySites;

        if(!favorites){
            // remover los sites favoritos de los cercanos
            for (BNSite favorite : this.favouriteSites.values()) {
                if(sites.containsKey(favorite.getIdentifier())){
                    sites.remove(favorite.getIdentifier());
                }
            }
        }

        // retornar la lista de sites
        return sites;
    }

    /****************** Sites near by end ******************/


    /****************** Manage favourites start ******************/

    public boolean likeBNSite(String identifier){
        BNSite site = getBNSite(identifier);
        site.setUserLiked(true);

        if(this.pendingUnlikeSites.containsKey(identifier)){
            this.pendingUnlikeSites.remove(identifier);
        }else{
            if(!this.pendingLikeSites.containsKey(identifier)){
                this.pendingLikeSites.put(identifier, site);
            }
        }

//        if(this.unlikedSites.containsKey(identifier)){
//            this.unlikedSites.remove(identifier);
//        }else{
//            if(!this.likedSites.containsKey(identifier)){
//                this.likedSites.put(identifier, site);
//            }
//        }

        return addFavouriteBNSite(site);
    }

    public boolean unlikeBNSite(String identifier){
        BNSite site = getBNSite(identifier);
        site.setUserLiked(false);

        if(this.pendingLikeSites.containsKey(identifier)){
            this.pendingLikeSites.remove(identifier);
        }else{
            if(!this.pendingUnlikeSites.containsKey(identifier)){
                this.pendingUnlikeSites.put(identifier, site);
            }
        }

//        if(this.likedSites.containsKey(identifier)){
//            this.likedSites.remove(identifier);
//        }else{
//            if(!this.unlikedSites.containsKey(identifier)){
//                this.unlikedSites.put(identifier, site);
//            }
//        }

        boolean added = addNearByBNSite(site);

        return removeFavouriteBNSite(identifier);
    }

    /****************** Manage favourites end ******************/


    /****************** Sites favourites start ******************/

    public void setFavouriteBNSites(LinkedHashMap<String, BNSite> sites) {
        // remover los sites favoritos de los cercanos
        for (BNSite site : sites.values()) {
            if(this.nearBySites.containsKey(site.getIdentifier())){
                nearBySites.remove(site.getIdentifier());
            }
        }
        // reemplazar la coleccion completa de sites
        this.favouriteSites = sites;
    }

    public int addFavouriteBNSites(LinkedHashMap<String, BNSite> sites) {
        // agregar sites a la coleccion
        // TODO solo los que no existian previamente
        this.favouriteSites = sites;
        // TODO retornar el numero de sites agregados a la coleccion
        return 0;
    }

    public boolean addFavouriteBNSite(BNSite site) {
        boolean added = false;
        // agregar un site a la coleccion
        if(!this.favouriteSites.containsKey(site.getIdentifier())){
            this.favouriteSites.put(site.getIdentifier(), site);
            added = true;
        }
        // retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return added;
    }

    public BNSite getFavouriteBNSite(String identifier) {
        // TODO obtener un site por su identificador
        return null;
    }

    public boolean removeFavouriteBNSite(String identifier) {
        boolean removed = false;
        // remover un site de la coleccion
        if(!this.favouriteSites.containsKey(identifier)){
            this.favouriteSites.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return removed;
    }

    public LinkedHashMap<String,BNSite> getFavouriteBNSites(){
        // retornar la lista de sites
        return this.favouriteSites;
    }

    /****************** Sites favourites end ******************/


    /****************** Showcases start ******************/

    public void setBNShowcases(LinkedHashMap<String, BNShowcase> showcases) {
        // reemplazar la coleccion completa de showcases
        this.showcases = showcases;
    }

    public int addBNShowcases(LinkedHashMap<String, BNShowcase> showcases) {
        // agregar showcases a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNShowcase showcase : showcases.values()) {
            if(addBNShowcase(showcase)){
                added++;
            }
        }
        // retornar el numero de showcases agregados a la coleccion
        return added;
    }

    public boolean addBNShowcase(BNShowcase showcase) {
        boolean added = false;
        // agregar un showcase a la coleccion
        if(!this.showcases.containsKey(showcase.getIdentifier())){
            this.showcases.put(showcase.getIdentifier(), showcase);
            added = true;
        }
        // retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return added;
    }

    public BNShowcase getBNShowcase(String identifier) {
        // obtener un showcase por su identificador
        return this.showcases.get(identifier);
    }

    public boolean removeBNShowcase(String identifier) {
        // TODO remover un showcase de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public LinkedHashMap<String,BNShowcase> getBNShowcases(){
        // retornar la lista de showcases
        return this.showcases;
    }

    /****************** Showcases end ******************/


    /****************** Organizations start ******************/

    public void setBNOrganizations(LinkedHashMap<String, BNOrganization> organizations) {
        // reemplazar la coleccion completa de organizations
        this.organizations = organizations;
    }

    public int addBNOrganizations(LinkedHashMap<String, BNOrganization> organizations) {
        // agregar organizations a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNOrganization organization : organizations.values()) {
            if(addBNOrganization(organization)){
                added++;
            }
        }
        // retornar el numero de organizations agregados a la coleccion
        return added;
    }

    public boolean addBNOrganization(BNOrganization organization) {
        boolean added = false;
        // agregar una organization a la coleccion
        if(!this.organizations.containsKey(organization.getIdentifier())){
            this.organizations.put(organization.getIdentifier(), organization);
            added = true;
        }
        // retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return added;
    }

    public BNOrganization getBNOrganization(String identifier) {
        // obtener una organization por su identificador
        return this.organizations.get(identifier);
    }

    public boolean removeBNOrganization(String identifier) {
        // TODO remover una organization de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public LinkedHashMap<String,BNOrganization> getBNOrganizations(){
        // retornar la lista de organizations
        return this.organizations;
    }

    /****************** Organizations end ******************/


    /****************** Elements start ******************/

    public void setBNElements(LinkedHashMap<String, BNElement> elements) {
        // reemplazar la coleccion completa de elements
        this.elements = elements;
    }

    public int addBNElements(LinkedHashMap<String, BNElement> elements) {
        // agregar elements a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNElement element : elements.values()) {
            if(addBNElement(element)){
                added++;
            }
        }
        // retornar el numero de elements agregados a la coleccion
        return added;
    }

    public boolean addBNElement(BNElement element) {
        boolean added = false;
        // agregar un element a la coleccion
        if(!this.elements.containsKey(element.getIdentifier())){
            this.elements.put(element.getIdentifier(), element);
            added = true;
        }
        // retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return added;
    }

    public BNElement getBNElement(String identifier) {
        // obtener un element por su identificador
        return this.elements.get(identifier);
    }

    public boolean removeBNElement(String identifier) {
        // TODO remover un element de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public LinkedHashMap<String,BNElement> getBNElements(){
        // retornar la lista de elements
        return this.elements;
    }

    /****************** Elements end ******************/


    /****************** Elements start ******************/

    public void setBNElementsId(LinkedHashMap<String, BNElement> elements) {
        // reemplazar la coleccion completa de elements
        this.elements_by_id = elements;
    }

    public int addBNElementsId(LinkedHashMap<String, BNElement> elements) {
        // TODO agregar elements a la coleccion (solo los que no existian previamente)
        this.elements_by_id = elements;
        // TODO retornar el numero de elements agregados a la coleccion
        return 0;
    }

    public boolean addBNElementId(BNElement element) {
        // TODO agregar un element a la coleccion
        this.elements_by_id.put(element.get_id(), element);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNElement getBNElementId(String id) {
        // obtener un element por su identificador
        return this.elements_by_id.get(id);
    }

    public boolean removeBNElementId(String id) {
        // TODO remover un element de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public LinkedHashMap<String,BNElement> getBNElementsId(){
        // retornar la lista de elements
        return this.elements_by_id;
    }

    /****************** Elements end ******************/


    /****************** Highlights start ******************/

    public void setBNHighlights(List<BNHighlight> highlights) {
        // reemplazar la coleccion completa de highlights
        this.highlights = highlights;
    }

//    public int addBNHighlights(LinkedHashMap<String, BNHighlight> highlights) {
//        // TODO agregar highlights a la coleccion (solo los que no existian previamente)
//        this.highlights = highlights;
//        // TODO retornar el numero de highlights agregados a la coleccion
//        return 0;
//    }

//    public boolean addBNHighlights(BNHighlight highlight) {
//        // TODO agregar un highlight a la coleccion
//        this.highlights.put(highlight.getIdentifier(), highlight);
//        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
//        return true;
//    }

    public BNHighlight getBNHighlight(String identifier) {
        // TODO obtener un element por su identificador
        return null;
    }

    public boolean removeBNHighlight(String identifier) {
        // TODO remover un highlight de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public List<BNHighlight> getBNHighlights(){
        // retornar la lista de highlights
        return this.highlights;
    }

    /****************** Highlights end ******************/


    /****************** Elements favourites start ******************/

    public void setFavouriteBNElements(LinkedHashMap<String, BNElement> elements) {
        // reemplazar la coleccion completa de elements
        this.favouriteElements = elements;
    }

    public int addFavouriteBNElements(LinkedHashMap<String, BNElement> elements) {
        // agregar elements a la coleccion
        // TODO solo los que no existian previamente
        this.favouriteElements = elements;
        // TODO retornar el numero de sites agregados a la coleccion
        return 0;
    }

    public boolean addFavouriteBNElement(BNElement element) {
        // agregar un element a la coleccion
        this.favouriteElements.put(element.getIdentifier(), element);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNElement getFavouriteBNElement(String identifier) {
        // obtener un element por su identificador
        return this.favouriteElements.get(identifier);
    }

    public boolean removeFavouriteBNElement(String identifier) {
        // TODO remover un element de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public LinkedHashMap<String,BNElement> getFavouriteBNElements(){
        // retornar la lista de elements
        return this.favouriteElements;
    }

    /****************** Elements favourites end ******************/


    /****************** Categories start ******************/

    public void setBNCategories(LinkedHashMap<String, BNCategory> categories) {
        // reemplazar la coleccion completa de categories
        this.categories = categories;
    }

    public int addBNCategories(LinkedHashMap<String, BNCategory> categories) {
        // TODO agregar categories a la coleccion (solo los que no existian previamente)
        this.categories = categories;
        // TODO retornar el numero de categories agregados a la coleccion
        return 0;
    }

    public boolean addBNCategory(BNCategory category) {
        // TODO agregar una category a la coleccion
        this.categories.put(category.getIdentifier(), category);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNCategory getBNCategory(String identifier) {
        // obtener una category por su identificador
        return this.categories.get(identifier);
    }

    public boolean removeBNCategory(String identifier) {
        // TODO remover una category de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return false;
    }

    public LinkedHashMap<String,BNCategory> getBNCategories(){
        // retornar la lista de categories
        return this.categories;
    }

    /****************** Categories end ******************/

}
