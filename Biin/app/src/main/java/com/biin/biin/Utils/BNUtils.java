package com.biin.biin.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Typeface;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNUtils {

    private static final String TAG = "BNUtils";

    private static int width = 0;
    private static float density = 1;

    private static Typeface lato_light;
    private static Typeface lato_regular;
    private static Typeface lato_black;
    private static String version = "";

    public static void setTypefaces(Context context){
        lato_light = Typeface.createFromAsset(context.getAssets(),"Lato-Light.ttf");
        lato_regular = Typeface.createFromAsset(context.getAssets(),"Lato-Regular.ttf");
        lato_black = Typeface.createFromAsset(context.getAssets(),"Lato-Black.ttf");
    }

    public static Typeface getLato_black() {
        return lato_black;
    }

    public static Typeface getLato_light() {
        return lato_light;
    }

    public static Typeface getLato_regular() {
        return lato_regular;
    }

    public static int getColorFromString(String values){
        // obtener el color a partir del string en formato RGB ej.: "228, 27, 37"
        int color = Color.rgb(0,0,0);
        if(StringUtils.countMatches(values, ",") == 2) {
            List<String> colorValues = Arrays.asList(values.split(","));
            color = Color.rgb(Integer.parseInt(colorValues.get(0).trim()),
                    Integer.parseInt(colorValues.get(1).trim()),
                    Integer.parseInt(colorValues.get(2).trim()));
        }
        return color;
    }

    public static Date getDateFromString(String value){
        // obtener la fecha a partir del string recibido ej.: "2016-04-29 22:45:26"
        Date fecha = new Date();
        String dateFotmat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(dateFotmat);
        try {
            fecha = format.parse(value);
        } catch (Exception e) {
            Log.e(TAG, "Error convirtiendo el string (" + value + ") a fecha en formato '" + dateFotmat + "'");
        }
        return fecha;
    }

    public static Boolean getBooleanFromString(String value){
        // obtener un valor booleano a partir del string recibido ej.: "1" / "0"
        return "1".equals(value);
    }

    public static ColorMatrix getColorMatrixFromColor(int color){
        ColorMatrix matrix = new ColorMatrix(new float[] {
                Color.red(color), 0, 0, 0, 0,
                0, Color.green(color), 0, 0, 0,
                0, 0, Color.blue(color), 0, 0,
                0, 0, 0, 1, 0,
        });
        return matrix;
    }

    public static void setWidth(int pwidth){
        width = pwidth;
    }

    public static int getWidth(){
        return width;
    }

    public static float getDensity() {
        return density;
    }

    public static void setDensity(float density) {
        BNUtils.density = density;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        BNUtils.version = version;
    }

    public static List<String> getIdentifiers(JSONArray array){
        List<String> identifiers = new ArrayList<>();
        try{
            for(int i = 0; i < array.length(); i++){
                JSONObject object = (JSONObject) array.get(i);
                identifiers.add(object.getString("identifier"));
            }
        }catch (JSONException e){
            Log.e(TAG, "Error parseando el JSON.", e);
        }
        return identifiers;
    }

    public static boolean calculateContrast(int background, int primary, int secondary){
        if(ColorUtils.calculateContrast(background, primary) > ColorUtils.calculateContrast(background, secondary)){
            return true;
        }else{
            return false;
        }
    }

    public static String getColorCSS(int color){
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return String.format("rgb(%d, %d, %d)", r, g, b);
    }

    public static String getLanguaje(){
        // Por el momento sólo español, cualquier otro caso se retornará inglés
        return Locale.getDefault().getLanguage().equals("es") ? "es" : "en";
    }

    public static String getActionDateFormat(){
        return "yyyy-MM-dd'T'HH:mm:ss'Z'";
    }

    public static String getDisplayDateFormat(){
        return Locale.getDefault().getLanguage().equals("es") ? "dd/MM/yyyy" : "MM/dd/yyyy";
    }

    public static String getUserDateFormat(){
        return Locale.getDefault().getLanguage().equals("es") ? "dd MMM yyyy" : "MMM dd yyyy";
    }

    public class BNStringExtras {
        public static final String BNBiinie = "BNBiinieIdentifier";
        public static final String BNSite = "BNSiteIdentifier";
        public static final String BNShowOthers = "BNShowOtherSites";
        public static final String BNFavorites = "BNFavoriteSites";
        public static final String BNElement = "BNElementIdentifier";
        public static final String BNShowMore = "BNShowMoreFromSite";
        public static final String BNCategory = "BNCategoryIdentifier";
        public static final String BNShowcase = "BNShowcaseIdentifier";
    }

    public class BNStringTypes {
        public static final String NearSites = "NearSites";
        public static final String FavouriteSites = "FavouriteSites";
    }

}
