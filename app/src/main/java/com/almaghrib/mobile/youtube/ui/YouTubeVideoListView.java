package com.almaghrib.mobile.youtube.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.almaghrib.mobile.R;

/**
 * A custom ListView that takes a list of videos to display</br>
 * As you can see you don't call setAdapter you should call setVideos and
 * the rest is done for you.</br>
 * </br>
 * Although this is a simple custom view it is good practice to always use custom views when you can
 * it allows you to encapsulate your work and keep your activity as a delegate whenever possible</br>
 * This list could be further customised without any hard graft, whereas if you had put this into the activity</br>
 * it would have been a real pain to pull out further down the road.</br>
 * </br>
 * One example is we could switch out the adapter we are using, to something
 * that displays scrolling images or whatever, and our activity never need know!</br>
 *
 */
public class YouTubeVideoListView extends ListView {
    final View mLoadingFooterView =
            LayoutInflater.from(getContext()).inflate(R.layout.footer_loading_spinner, null);
 
    public YouTubeVideoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public YouTubeVideoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public YouTubeVideoListView(Context context) {
        super(context);
    }

    public void removeLoadingFooterView() {
        removeFooterView(mLoadingFooterView);
    }

    public void addLoadingFooterView() {
        addFooterView(mLoadingFooterView);
    }
}