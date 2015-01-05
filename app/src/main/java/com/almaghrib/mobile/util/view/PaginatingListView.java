package com.almaghrib.mobile.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.almaghrib.mobile.R;

/**
 * A custom ListView which allows the addition and removal of a footer which
 * shows a spinner whilst the list loads the remaining elements.
 */
public class PaginatingListView extends ListView {
    final View mLoadingFooterView =
            LayoutInflater.from(getContext()).inflate(R.layout.footer_loading_spinner, null);
 
    public PaginatingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public PaginatingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public PaginatingListView(Context context) {
        super(context);
    }

    public void removeLoadingFooterView() {
        removeFooterView(mLoadingFooterView);
    }

    public void addLoadingFooterView() {
        addFooterView(mLoadingFooterView);
    }
}