package com.biin.biin.CardView;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by ramirezallan on 5/12/16.
 */
public class CardRecyclerView extends SnappingRecyclerView {

    public CardRecyclerView(Context context) {
        super(context);
    }

    public CardRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean fling(int velocityX, int velocityY)
    {
        velocityX *= 1; // (between 0 for no fling, and 1 for normal fling, or more for faster fling).
        return super.fling(velocityX, velocityY);
    }
}
