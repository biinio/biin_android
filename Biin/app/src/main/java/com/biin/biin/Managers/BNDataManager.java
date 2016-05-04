package com.biin.biin.Managers;

import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;

import java.util.HashMap;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNDataManager {

    private static BNDataManager ourInstance = new BNDataManager();

    private Biinie biinie = new Biinie();

    private HashMap<String, BNSite> sites = new HashMap<>();
    private HashMap<String, BNOrganization> organizations = new HashMap<>();
    private HashMap<String, BNElement> elements = new HashMap<>();
    private HashMap<String, BNHighlight> highlights = new HashMap<>();
    private HashMap<String, BNCategory> categories = new HashMap<>();

    public static BNDataManager getInstance() {
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

    public void setBNSites(HashMap<String, BNSite> sites) {
        // reemplazar la coleccion completa de sites
        this.sites = sites;
    }

    public int addBNSites(HashMap<String, BNSite> sites) {
        // TODO agregar sites a la coleccion (solo los que no existian previamente)
        this.sites = sites;
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
        // TODO obtener un site por su identificador
        return null;
    }

    public boolean removeBNSite(String identifier) {
        // TODO remover un site de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return true;
    }

    public HashMap<String,BNSite> getBNSites(){
        // retornar la lista de sites
        return this.sites;
    }

    /****************** Sites end ******************/


    /****************** Organizations start ******************/

    public void setBNOrganizations(HashMap<String, BNOrganization> organizations) {
        // reemplazar la coleccion completa de organizations
        this.organizations = organizations;
    }

    public int addBNOrganizations(HashMap<String, BNOrganization> organizations) {
        // TODO agregar organizations a la coleccion (solo los que no existian previamente)
        this.organizations = organizations;
        // TODO retornar el numero de organizations agregados a la coleccion
        return 0;
    }

    public boolean addBNOrganization(BNOrganization organization) {
        // TODO agregar una organization a la coleccion
        this.organizations.put(organization.getIdentifier(), organization);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNOrganization getBNOrganization(String identifier) {
        // TODO obtener una organization por su identificador
        return null;
    }

    public boolean removeBNOrganization(String identifier) {
        // TODO remover una organization de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return true;
    }

    public HashMap<String,BNOrganization> getBNOrganizations(){
        // retornar la lista de organizations
        return this.organizations;
    }

    /****************** Organizations end ******************/


    /****************** Elements start ******************/

    public void setBNElements(HashMap<String, BNElement> elements) {
        // reemplazar la coleccion completa de elements
        this.elements = elements;
    }

    public int addBNElements(HashMap<String, BNElement> elements) {
        // TODO agregar elements a la coleccion (solo los que no existian previamente)
        this.elements = elements;
        // TODO retornar el numero de elements agregados a la coleccion
        return 0;
    }

    public boolean addBNElement(BNElement element) {
        // TODO agregar un element a la coleccion
        this.elements.put(element.getIdentifier(), element);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNElement getBNElement(String identifier) {
        // TODO obtener un element por su identificador
        return null;
    }

    public boolean removeBNElement(String identifier) {
        // TODO remover un element de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return true;
    }

    public HashMap<String,BNElement> getBNElements(){
        // retornar la lista de elements
        return this.elements;
    }

    /****************** Elements end ******************/


    /****************** Highlights start ******************/

    public void setBNHighlightss(HashMap<String, BNHighlight> highlights) {
        // reemplazar la coleccion completa de highlights
        this.highlights = highlights;
    }

    public int addBNHighlightss(HashMap<String, BNHighlight> highlights) {
        // TODO agregar highlights a la coleccion (solo los que no existian previamente)
        this.highlights = highlights;
        // TODO retornar el numero de highlights agregados a la coleccion
        return 0;
    }

    public boolean addBNHighlights(BNHighlight highlight) {
        // TODO agregar un highlight a la coleccion
        this.highlights.put(highlight.getIdentifier(), highlight);
        // TODO retornar true si se agrego o false si no se agrego (por ejemplo si ya existia)
        return true;
    }

    public BNHighlight getBNHighlight(String identifier) {
        // TODO obtener un element por su identificador
        return null;
    }

    public boolean removeBNHighlight(String identifier) {
        // TODO remover un highlight de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return true;
    }

    public HashMap<String,BNHighlight> getBNHighlights(){
        // retornar la lista de highlights
        return this.highlights;
    }

    /****************** Highlights end ******************/


    /****************** Categories start ******************/

    public void setBNCategories(HashMap<String, BNCategory> categories) {
        // reemplazar la coleccion completa de categories
        this.categories = categories;
    }

    public int addBNCategories(HashMap<String, BNCategory> categories) {
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
        // TODO obtener una category por su identificador
        return null;
    }

    public boolean removeBNCategory(String identifier) {
        // TODO remover una category de la coleccion
        // TODO retornar true si se elimino o false si no se elimino (por ejemplo si no existia)
        return true;
    }

    public HashMap<String,BNCategory> getBNCategories(){
        // retornar la lista de categories
        return this.categories;
    }

    /****************** Categories end ******************/

}
