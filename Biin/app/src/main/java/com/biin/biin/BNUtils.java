package com.biin.biin;

import android.graphics.Color;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ramirezallan on 5/3/16.
 */
public class BNUtils {

    private static final String TAG = "BNUtils";

    private static int width = 0;

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
        // TODO obtener la fecha a partir del string recibido ej.: "2016-04-29 22:45:26"
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

    public static void setWidth(int pwidth){
        width = pwidth;
    }

    public static int getWidth(){
        return width;
    }

}
