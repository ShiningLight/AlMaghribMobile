package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarBannerModel;

import java.util.ArrayList;

public class SeminarsFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SeminarCardAdapter mAdapter;

    public static SeminarsFragment init() {
        final SeminarsFragment fragment = new SeminarsFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "Seminars"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.seminars_list_page, container, false);

        mRecyclerView = (RecyclerView) layoutView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SeminarCardAdapter(new ArrayList<AlMaghribUpcomingSeminarBannerModel>());
        mRecyclerView.setAdapter(mAdapter);

        return layoutView;
    }
}
