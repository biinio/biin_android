package com.biin.biin.Components;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ramirezallan on 6/5/16.
 */
public class BNScrollView extends NestedScrollView {
    public BNScrollView(Context context) {
        super(context);
    }

    public BNScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BNScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
//        super.requestChildFocus(child, focused);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
//        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        return false;
    }
}
