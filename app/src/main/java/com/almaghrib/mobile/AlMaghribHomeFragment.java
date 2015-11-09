package com.almaghrib.mobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.youtube.data.YouTubeApiUriRequestBuilder;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchModelContainer;
import com.android.volley.Request;

public class AlMaghribHomeFragment extends Fragment {

    public AlMaghribHomeFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.almaghrib_home_page, container, false);

        return layoutView;
    }

}
