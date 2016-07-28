package com.biin.biin.Components.Listeners;

import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by ramirezallan on 6/17/16.
 */
public class HighlightsPagerListener implements ViewPager.OnPageChangeListener {

    private static String TAG = "HighlightsPagerListener";
    private int length = 1;

    private IBNHighlightsListener listener;

    public HighlightsPagerListener(IBNHighlightsListener listener){
        this.listener = listener;
    }

    public void setLength(int length){
        this.length = length;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(listener != null){
            listener.onPageSelected(position % length);
        }else{
            Log.e(TAG, "El listener es nulo o no ha sido seteado.");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface IBNHighlightsListener{
        void onPageSelected(int position);
    }
}
