package com.example.wataru.room_memo;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * TODO: document your custom view class.
 */
public class ChildPagerMapView extends WebView {

    public ChildPagerMapView(Context context) {
        super(context);
    }

    public ChildPagerMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildPagerMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
