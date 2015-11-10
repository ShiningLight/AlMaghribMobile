package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OnlineSeminarsFragment extends Fragment{


    public static OnlineSeminarsFragment init() {
        final OnlineSeminarsFragment fragment = new OnlineSeminarsFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "Online"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.youtube_page, container, false);



        return layoutView;
    }
}
