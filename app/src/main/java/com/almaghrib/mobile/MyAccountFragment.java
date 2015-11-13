package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyAccountFragment extends Fragment {

    public static MyAccountFragment init() {
        final MyAccountFragment fragment = new MyAccountFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "My Account"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: trigger off fetch

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.my_account_page, container, false);


        return layoutView;
    }
}
