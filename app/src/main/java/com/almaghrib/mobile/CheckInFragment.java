package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribCheckInModelContainer;

import java.util.ArrayList;
import java.util.List;

public class CheckInFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CheckInCardAdapter mAdapter;

    private List<AlMaghribCheckInModelContainer> mDataset;

    public static SeminarsFragment init() {
        final SeminarsFragment fragment = new SeminarsFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "Check In"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: trigger off fetch
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.seminars_list_page, container, false);

        mRecyclerView = (RecyclerView) layoutView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDataset = new ArrayList<AlMaghribCheckInModelContainer>();

        // TODO : remove dummy data
        mDataset.add(new AlMaghribCheckInModelContainer("http://qrickit.com/api/qr?d=123&fgdcolor=76103C&qrsize=180",
                "IlmFest 2016", "January 8-10", "University of Toronto"));
        mDataset.add(new AlMaghribCheckInModelContainer("http://qrickit.com/api/qr?d=123&fgdcolor=76103C&qrsize=180",
                "World of Dreams", "January 1-10", "University of Egypt"));
        mDataset.add(new AlMaghribCheckInModelContainer("http://qrickit.com/api/qr?d=123&fgdcolor=76103C&qrsize=180",
                "World of Funnnn", "February 1-10", "Regents Park Hall"));

        mAdapter = new CheckInCardAdapter(getActivity().getApplicationContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        return layoutView;
    }
}
