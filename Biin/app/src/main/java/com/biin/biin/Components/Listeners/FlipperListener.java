package com.biin.biin.Components.Listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ramirezallan on 5/20/16.
 */
public class FlipperListener implements View.OnTouchListener {

    private GestureDetector mGestureDetector;

    public FlipperListener(GestureDetector mGestureDetector) {
        super();
        this.mGestureDetector = mGestureDetector;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }
}
