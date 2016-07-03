package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.biin.biin.Utils.BNUtils;

import java.util.List;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
//        String scheme = data.getScheme(); // "http"
//        String host = data.getHost(); // "biin.io"
        List<String> params = data.getPathSegments();
        String type = params.get(0); // "type"
        String identifier = params.get(1); // "identifier"

        if(type.equals("elements")){
            Intent i = new Intent(this, ElementsActivity.class);
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(BNUtils.BNStringExtras.BNElement, identifier);
            editor.commit();
            i.putExtra(BNUtils.BNStringExtras.BNShowMore, false); //mostrar mas del mismo site
            startActivity(i);
        }else if(type.equals("sites")){
            Intent i = new Intent(this, SitesActivity.class);
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(BNUtils.BNStringExtras.BNSite, identifier);
            editor.commit();
            i.putExtra(BNUtils.BNStringExtras.BNShowOthers, false); //mostrar mas sites del mismo organization
            startActivity(i);
        }

        finish();
    }
}
