package com.biin.biin.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNBeacon;
import com.biin.biin.Entities.BNCategory;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Entities.BNHighlight;
import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.Entities.BNNotification;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.BNShowcase;
import com.biin.biin.Entities.BNSite;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.Listeners.BNLikesListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BNDataManager implements BNLikesListener.IBNLikesListener {

    private static final String TAG = "BNDataManager";
    private static BNDataManager ourInstance = new BNDataManager();

    private static boolean bluetoothPermissionAsked = false;

    private Biinie biinie = new Biinie();

    private LinkedHashMap<String, BNSite> sites = new LinkedHashMap<>();
    private LinkedHashMap<String, BNShowcase> showcases = new LinkedHashMap<>();
    private LinkedHashMap<String, BNOrganization> organizations = new LinkedHashMap<>();
    private LinkedHashMap<String, BNElement> elements = new LinkedHashMap<>();
    private LinkedHashMap<String, BNCategory> categories = new LinkedHashMap<>();
    private LinkedHashMap<String, BNGift> gifts = new LinkedHashMap<>();

    private List<BNHighlight> highlights = new ArrayList<>();
    private List<BNElement> favouriteElements = new ArrayList<>();
    private List<BNSite> nearBySites = new ArrayList<>();
    private List<BNSite> favouriteSites = new ArrayList<>();
    private List<BNBeacon> nearByBeacons = new ArrayList<>();
    private List<BNNotification> notifications = new ArrayList<>();
    private List<BNLoyalty> loyalties = new ArrayList<>();

    private LinkedHashMap<String, BNSite> pendingLikeSites = new LinkedHashMap<>();
    private LinkedHashMap<String, BNSite> pendingUnlikeSites = new LinkedHashMap<>();

    private LinkedHashMap<String, BNElement> pendingLikeElements = new LinkedHashMap<>();
    private LinkedHashMap<String, BNElement> pendingUnlikeElements = new LinkedHashMap<>();

    private static int nearBySitesVersion = 1;
    private static int favouriteSitesVersion = 1;
    private static int favouriteElementsVersion = 1;

    private BNLikesListener likesListener;

    protected static BNDataManager getInstance() {
        return ourInstance;
    }

    private BNDataManager() {
        likesListener = new BNLikesListener();
        likesListener.setListener(this);
    }


    /****************** Permissions start ******************/
    public static boolean isBluetoothPermissionAsked() {
        return bluetoothPermissionAsked;
    }

    public static void setBluetoothPermissionAsked() {
        bluetoothPermissionAsked = true;
    }
    /****************** Permissions end ******************/


    /****************** Biinie start ******************/

    public void setBiinie(Biinie biinie) {
        // reemplazar el biinie
        this.biinie = biinie;
        BNAppManager.getInstance().getAnalyticsManagerInstance().setBiinie(biinie);
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
        setNearByBeacons(getSitesBeacons(new ArrayList<>(sites.values())));
    }

    public int addBNSites(LinkedHashMap<String, BNSite> sites) {
        // agregar sites a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNSite site : sites.values()) {
            if(addBNSite(site)){
                added++;
            }
        }
        // retornar el numero de sites agregados a la coleccion
        return added;
    }

    public boolean addBNSite(BNSite site) {
        boolean added = false;
        // agregar un site a la coleccion
        if(!this.sites.containsKey(site.getIdentifier())){
            this.sites.put(site.getIdentifier(), site);
            added = true;
        }
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNSite getBNSite(String identifier) {
        // obtener un site por su identificador
        return this.sites.get(identifier);
    }

    public boolean removeBNSite(String identifier) {
        boolean removed = false;
        // remover un site de la coleccion
        if(this.sites.containsKey(identifier)){
            this.sites.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public LinkedHashMap<String,BNSite> getBNSites(){
        // retornar la lista de sites
        return this.sites;
    }

    /****************** Sites end ******************/


    /****************** Sites near by start ******************/

    public void setNearByBNSites(List<BNSite> sites) {
        nearBySitesVersion++;
        // reemplazar la coleccion completa de sites
        this.nearBySites.clear();
        for (BNSite site : sites) {
            if(!site.isUserLiked()) {
                addNearByBNSite(site);
            }else{
                addFavouriteBNSite(site);
            }
        }
    }

    public int addNearByBNSites(List<BNSite> sites) {
        nearBySitesVersion++;
        // agregar sites a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNSite site : sites) {
            if(addNearByBNSite(site)){
                added++;
            }
        }
        // retornar el numero de sites agregados a la coleccion
        return added;
    }

    public boolean addNearByBNSite(BNSite site) {
        return addNearByBNSite(site, -1);
    }

    public boolean addNearByBNSite(BNSite site, int position) {
        nearBySitesVersion++;
        boolean added = false;
        // agregar un site a la coleccion
        int index = nearBySitesContains(site.getIdentifier());
        if(index == -1){
            if(position > -1){
                this.nearBySites.add(position, site);
            }else {
                this.nearBySites.add(site);
            }
            added = true;
        }
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNSite getNearByBNSite(String identifier) {
        BNSite site = null;
        // obtener un site por su identificador
        int index = nearBySitesContains(identifier);
        if(index > -1){
            site = this.nearBySites.get(index);
        }
        return site;
    }

    public boolean removeNearByBNSite(String identifier) {
        nearBySitesVersion++;
        boolean removed = false;
        // remover un site de la coleccion
        int index = nearBySitesContains(identifier);
        if(index > -1){
            this.nearBySites.remove(index);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public List<BNSite> getNearByBNSites(){
        return this.nearBySites;
    }

    public int nearBySitesContains(String identifier){
        int index = -1;

        for (int i = 0; i < this.nearBySites.size(); i++) {
            if(this.nearBySites.get(i).getIdentifier().equals(identifier)){
                index = i;
            }
        }
        
        return index;
    }

    /****************** Sites near by end ******************/


    /****************** Sites favourites start ******************/

    public void setFavouriteBNSites(List<BNSite> sites) {
        favouriteSitesVersion++;
        // reemplazar la coleccion completa de sites
        this.favouriteSites = sites;
    }

    public int addFavouriteBNSites(List<BNSite> sites) {
        favouriteSitesVersion++;
        // agregar sites a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNSite site : sites) {
            if(addFavouriteBNSite(site)){
                added++;
            }
        }
        // retornar el numero de sites agregados a la coleccion
        return added;
    }

    public boolean addFavouriteBNSite(BNSite site) {
        return addFavouriteBNSite(site, -1);
    }

    public boolean addFavouriteBNSite(BNSite site, int position) {
        favouriteSitesVersion++;
        boolean added = false;
        // agregar un site a la coleccion
        int index = favouriteSitesContains(site.getIdentifier());
        if(index == -1){
            if(position > -1){
                this.favouriteSites.add(position, site);
            }else {
                this.favouriteSites.add(site);
            }
            added = true;
        }
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNSite getFavouriteBNSite(String identifier) {
        BNSite site = null;
        // obtener un site por su identificador
        int index = favouriteSitesContains(identifier);
        if(index > -1){
            site = this.favouriteSites.get(index);
        }
        return site;
    }

    public boolean removeFavouriteBNSite(String identifier) {
        favouriteSitesVersion++;
        boolean removed = false;
        // remover un site de la coleccion
        int index = favouriteSitesContains(identifier);
        if(index > -1){
            this.favouriteSites.remove(index);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public List<BNSite> getFavouriteBNSites(){
        // retornar la lista de sites
        return this.favouriteSites;
    }

    public int favouriteSitesContains(String identifier){
        /*int index = -1;

        for (int i = 0; i < this.favouriteSites.size(); i++) {
            if(this.favouriteSites.get(i).getIdentifier().equals(identifier)){
                index = i;
            }
        }

        return index;*/
        return this.favouriteSites.indexOf(getBNSite(identifier));
    }

    /****************** Sites favourites end ******************/


    /****************** Manage favourites start ******************/

    public boolean likeBNSite(String identifier){
        likeSite(identifier, true);

        BNSite site = getBNSite(identifier);

        int index = this.nearBySites.indexOf(site);
        if(index > -1){
            this.nearBySites.remove(index);
            nearBySitesVersion++;
        }

        site.setUserLiked(true);
        site.setLikeDate(Calendar.getInstance().getTime());
        return addFavouriteBNSite(site, 0);
    }

    public boolean unlikeBNSite(String identifier){
        likeSite(identifier, false);

        BNSite site = getBNSite(identifier);

        int index = this.favouriteSites.indexOf(site);
        if(index > -1){
            site.setUserLiked(false);
            site.setLikeDate(null);
            this.favouriteSites.remove(index);
            favouriteSitesVersion++;
        }

        return addNearByBNSite(site, 0);
    }

    private void likeSite(final String identifier, final boolean liked) {
        BNSite site = getBNSite(identifier);

        String url = BNAppManager.getInstance().getNetworkManagerInstance().getLikeUrl(biinie.getIdentifier(), liked);
        Log.d(TAG, url);

        JSONObject request = new JSONObject();
        try {
            JSONObject model = site.getModel();
            request.put("model", model);
        } catch (JSONException e) {
            Log.e(TAG, "Error:" + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                request,
                likesListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLikeError(error, identifier, liked);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, "LikeSite");
    }

    private void onLikeError(VolleyError error, String identifier, boolean liked) {
        Log.e(TAG, "Error:" + error.getMessage());
        if (liked) {
            addPendingLikeSite(identifier);
        } else {
            addPendingUnlikeSite(identifier);
        }
    }

    public void addPendingLikeSite(String identifier){
        if(this.pendingUnlikeSites.containsKey(identifier)){
            this.pendingUnlikeSites.remove(identifier);
        }else{
            if(!this.pendingLikeSites.containsKey(identifier)){
                BNSite site = getBNSite(identifier);
                site.setUserLiked(true);
                this.pendingLikeSites.put(identifier, site);
            }
        }
    }

    public void addPendingUnlikeSite(String identifier){
        if(this.pendingLikeSites.containsKey(identifier)){
            this.pendingLikeSites.remove(identifier);
        }else{
            if(!this.pendingUnlikeSites.containsKey(identifier)){
                BNSite site = getBNSite(identifier);
                site.setUserLiked(false);
                this.pendingUnlikeSites.put(identifier, site);
            }
        }
    }

    public void addPendingLikeElement(String identifier){
        if(this.pendingUnlikeElements.containsKey(identifier)){
            this.pendingUnlikeElements.remove(identifier);
        }else{
            if(!this.pendingLikeElements.containsKey(identifier)){
                BNElement element = getBNElement(identifier);
                element.setUserLiked(true);
                this.pendingLikeElements.put(identifier, element);
            }
        }
    }

    public void addPendingUnlikeElement(String identifier){
        if(this.pendingLikeElements.containsKey(identifier)){
            this.pendingLikeElements.remove(identifier);
        }else{
            if(!this.pendingUnlikeElements.containsKey(identifier)){
                BNElement element = getBNElement(identifier);
                element.setUserLiked(false);
                this.pendingUnlikeElements.put(identifier, element);
            }
        }
    }

    public int getNearBySitesVersion() {
        return nearBySitesVersion;
    }

    public int getFavouriteSitesVersion() {
        return favouriteSitesVersion;
    }

    public int getFavouriteElementsVersion() {
        return favouriteElementsVersion;
    }

    /****************** Manage favourites end ******************/


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
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNShowcase getBNShowcase(String identifier) {
        // obtener un showcase por su identificador
        return this.showcases.get(identifier);
    }

    public boolean removeBNShowcase(String identifier) {
        boolean removed = false;
        // remover un showcase de la coleccion
        if(this.showcases.containsKey(identifier)){
            this.showcases.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
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
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNOrganization getBNOrganization(String identifier) {
        // obtener una organization por su identificador
        return this.organizations.get(identifier);
    }

    public boolean removeBNOrganization(String identifier) {
        boolean removed = false;
        // remover un organization de la coleccion
        if(this.organizations.containsKey(identifier)){
            this.organizations.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
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
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNElement getBNElement(String identifier) {
        // obtener un element por su identificador
        return this.elements.get(identifier);
    }

    public boolean removeBNElement(String identifier) {
        boolean removed = false;
        // remover un element de la coleccion
        if(this.elements.containsKey(identifier)){
            this.elements.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public LinkedHashMap<String,BNElement> getBNElements(){
        // retornar la lista de elements
        return this.elements;
    }

    /****************** Elements end ******************/


    /****************** Highlights start ******************/

    public void setBNHighlights(List<BNHighlight> highlights) {
        // reemplazar la coleccion completa de highlights
        this.highlights = highlights;
    }

    public List<BNHighlight> getBNHighlights(){
        // retornar la lista de highlights
        return this.highlights;
    }

    /****************** Highlights end ******************/


    /****************** Elements favourites start ******************/

    public void setFavouriteBNElements(List<BNElement> elements) {
        favouriteElementsVersion++;
        // reemplazar la coleccion completa de elements
        this.favouriteElements.clear();
        for (BNElement element : elements) {
            addFavouriteBNElement(element);
        }
        // reemplazar la coleccion completa de elements
        this.favouriteElements = elements;
    }

    public int addFavouriteBNElements(List<BNElement> elements) {
        favouriteElementsVersion++;
        // agregar elements a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNElement element : elements) {
            if(addFavouriteBNElement(element)){
                added++;
            }
        }
        // retornar el numero de elements agregados a la coleccion
        return added;
    }

    public boolean addFavouriteBNElement(BNElement element) {
        return addFavouriteBNElement(element, -1);
    }

    public boolean addFavouriteBNElement(BNElement element, int position) {
        favouriteElementsVersion++;
        boolean added = false;
        // agregar un element a la coleccion
        int index = favouriteElementsContains(element.getIdentifier());
        if(index == -1){
            if(position > -1) {
                this.favouriteElements.add(position, element);
            }else{
                this.favouriteElements.add(element);
            }
            added = true;
        }
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNElement getFavouriteBNElement(String identifier) {
        BNElement element = null;
        // obtener un element por su identificador
        int index = favouriteElementsContains(identifier);
        if(index > -1){
            element = this.favouriteElements.get(index);
        }
        return element;
    }

    public boolean removeFavouriteBNElement(String identifier) {
        favouriteElementsVersion++;
        boolean removed = false;
        // remover un element de la coleccion
        int index = favouriteElementsContains(identifier);
        if(index > -1){
            this.favouriteElements.remove(index);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public List<BNElement> getFavouriteBNElements(){
        // retornar la lista de elements
        return this.favouriteElements;
    }

    public int favouriteElementsContains(String identifier){
        int index = -1;

        for (int i = 0; i < this.favouriteElements.size(); i++) {
            if(this.favouriteElements.get(i).getIdentifier().equals(identifier)){
                index = i;
            }
        }

        return index;
    }

    /****************** Elements favourites end ******************/


    /****************** Categories start ******************/

    public void setBNCategories(LinkedHashMap<String, BNCategory> categories) {
        // reemplazar la coleccion completa de categories
        this.categories = categories;
    }

    public int addBNCategories(LinkedHashMap<String, BNCategory> categories) {
        // agregar categories a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNCategory category : categories.values()) {
            if(addBNCategory(category)){
                added++;
            }
        }
        // retornar el numero de categories agregados a la coleccion
        return added;
    }

    public boolean addBNCategory(BNCategory category) {
        boolean added = false;
        // agregar una category a la coleccion
        if(!this.categories.containsKey(category.getIdentifier())){
            this.categories.put(category.getIdentifier(), category);
            added = true;
        }
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNCategory getBNCategory(String identifier) {
        // obtener una category por su identificador
        return this.categories.get(identifier);
    }

    public boolean removeBNCategory(String identifier) {
        boolean removed = false;
        // remover una category de la coleccion
        if(this.categories.containsKey(identifier)){
            this.categories.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public LinkedHashMap<String,BNCategory> getBNCategories(){
        // retornar la lista de categories
        return this.categories;
    }

    /****************** Categories end ******************/


    /****************** NPS start ******************/

    public void setNpsDateTime(String identifier){
        //TODO salvar en sqlite
    }

    public void removeNpsDateTime(String identifier){
        //TODO salvar en sqlite
    }

    public boolean isNpsAvailable(String identifier){
        //TODO obtener de sqlite
        return true;
    }

    /****************** NPS end ******************/


    /****************** Beacons start ******************/

    public List<BNBeacon> getNearByBeacons() {
        return nearByBeacons;
    }

    public void setNearByBeacons(List<BNBeacon> nearByBeacons) {
        this.nearByBeacons = nearByBeacons;
    }

    private List<BNBeacon> getSitesBeacons(List<BNSite> sites){
        List<BNBeacon> list = new ArrayList<>();
        for (BNSite site : sites) {
            BNBeacon beacon = new BNBeacon();
            beacon.setIdentfier(site.getOrganization().getName());
            beacon.setUUID("aabbccdd-a101-b202-c303-aabbccddeeff");
            beacon.setMajor(site.getMajor());
            beacon.setMinor(0);
            list.add(beacon);
        }
        return list;
    }

    public BNSite getBNSiteByMajor(int major) {
        BNSite result = null;
        // obtener un site por su major
        for (BNSite site : this.sites.values()) {
            if(site.getMajor() == major){
                result = site;
            }
        }
        return result;
    }

    /****************** Beacons end ******************/


    /****************** Gifts start ******************/

    public void setBNGifts(LinkedHashMap<String, BNGift> gifts) {
        // reemplazar la coleccion completa de gifts
        this.gifts = gifts;
    }

    public int addBNGifts(LinkedHashMap<String, BNGift> gifts) {
        // agregar gifts a la coleccion (solo los que no existian previamente)
        int added = 0;
        for (BNGift gift : gifts.values()) {
            if(addBNGift(gift)){
                added++;
            }
        }
        // retornar el numero de gifts agregados a la coleccion
        return added;
    }

    public boolean addBNGift(BNGift gift) {
        return addBNGift(gift, false);
    }

    public boolean addBNGift(BNGift gift, boolean start) {
        boolean added = false;
        // agregar un gift a la coleccion
        if(!this.gifts.containsKey(gift.getIdentifier())){
            if(start) {
                LinkedHashMap<String, BNGift> resp = (LinkedHashMap<String, BNGift>) this.gifts.clone();
                this.gifts.clear();
                this.gifts.put(gift.getIdentifier(), gift);
                this.gifts.putAll(resp);
            }else {
                this.gifts.put(gift.getIdentifier(), gift);
            }
            added = true;
        }
        // retornar true si se agrego o false si no se agrego
        return added;
    }

    public BNGift getBNGift(String identifier) {
        // obtener un gift por su identificador
        return this.gifts.get(identifier);
    }

    public boolean removeBNGift(String identifier) {
        boolean removed = false;
        // remover un gift de la coleccion
        if(this.gifts.containsKey(identifier)){
            this.gifts.remove(identifier);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public LinkedHashMap<String,BNGift> getBNGifts(){
        // retornar la lista de gifts
        return this.gifts;
    }

    /****************** Gifts end ******************/


    /****************** Notifications start ******************/

    public void setBNNotifications(List<BNNotification> notifications) {
        // reemplazar la coleccion completa de notifications
        this.notifications = notifications;
    }

    public int addBNNotifications(List<BNNotification> notifications) {
        // agregar notifications a la coleccion (solo las que no existian previamente)
        int added = 0;
        for (BNNotification notification : notifications) {
            if(addBNNotification(notification)){
                added++;
            }
        }
        // retornar el numero de notifications agregadas a la coleccion
        return added;
    }

    public boolean addBNNotification(BNNotification notification) {
        return addBNNotification(notification, false);
    }

    public boolean addBNNotification(BNNotification notification, boolean start) {
        // agregar una notification a la coleccion
        if(start) {
            this.notifications.add(0, notification);
        }else {
            this.notifications.add(notification);
        }
        return true;
    }

    public BNNotification getBNNotification(int position) {
        // obtener una notification por su posicion
        return this.notifications.get(position);
    }

    public boolean removeBNNotification(int position) {
        boolean removed = false;
        // remover una notification de la coleccion
        if(this.notifications.size() > position){
            this.notifications.remove(position);
            removed = true;
        }
        // retornar true si se elimino o false si no se elimino
        return removed;
    }

    public List<BNNotification> getBNNotifications(){
        // retornar la lista de notifications
        return this.notifications;
    }

    /****************** Gifts end ******************/


    /****************** Loyalties start ******************/

    public void setBNLoyalties(List<BNLoyalty> loyalties) {
        // reemplazar la coleccion completa de loyalties
        this.loyalties = loyalties;
    }

    public List<BNLoyalty> getBNLoyalties(){
        // retornar la lista de loyalties
        return this.loyalties;
    }

    /****************** Loyalties end ******************/


    /****************** Badges start ******************/

    public int getGiftsBadge(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        int giftsBadge = preferences.getInt(BNUtils.BNStringExtras.BNGiftBadge, 0);
        return giftsBadge;
    }

    public int incrementGiftsBadge(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        int giftsBadge = preferences.getInt(BNUtils.BNStringExtras.BNGiftBadge, 0);
        giftsBadge++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(BNUtils.BNStringExtras.BNGiftBadge, giftsBadge);
        editor.commit();
        return giftsBadge;
    }

    public void clearGiftsBadge(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(BNUtils.BNStringExtras.BNGiftBadge, 0);
        editor.commit();
    }

    public int getNotificationsBadge(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        int notificationsBadge = preferences.getInt(BNUtils.BNStringExtras.BNNotificationBadge, 0);
        return notificationsBadge;
    }

    public int incrementNotificationsBadge(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        int notificationsBadge = preferences.getInt(BNUtils.BNStringExtras.BNNotificationBadge, 0);
        notificationsBadge++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(BNUtils.BNStringExtras.BNNotificationBadge, notificationsBadge);
        editor.commit();
        return notificationsBadge;
    }

    public void clearNotificationsBadge(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(BNUtils.BNStringExtras.BNNotificationBadge, 0);
        editor.commit();
    }

    /****************** Badges end ******************/



    @Override
    public void onLikeResponseOk() {

    }

    @Override
    public void onLikeResponseError() {

    }

}
