package com.biin.biin;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.Volley.LruBitmapCache;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by ramirezallan on 5/2/16.
 */
public class BiinApp extends Application {

    public static final String TAG = "BIIN";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static BiinApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        KontaktSDK.initialize(getResources().getString(R.string.kontakt_io_api_key));

        File cacheDir = StorageUtils.getCacheDirectory(this);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(50 * 1024 * 1024))
                .memoryCacheSize(50 * 1024 * 1024)
                .memoryCacheSizePercentage(20)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(300 * 1024 * 1024)
                .diskCacheFileCount(500)
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

        BNUtils.setTypefaces(getBaseContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static synchronized BiinApp getInstance() {
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
