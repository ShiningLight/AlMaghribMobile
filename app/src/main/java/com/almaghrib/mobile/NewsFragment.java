package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribNewsModelContainer;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<AlMaghribNewsModelContainer> mDataset;

    public static NewsFragment init() {
        final NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "News"; //TODO: use strings
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

        mDataset = new ArrayList<AlMaghribNewsModelContainer>();

        // TODO : remove dummy data
        mDataset.add(new AlMaghribNewsModelContainer(null, "AlMaghrib launches in Australia", "Admin", "ef sf fuifehf ef hw wf", null));
        mDataset.add(new AlMaghribNewsModelContainer(null, "Global Impact Day makes an entrance", "Admin", "ef sf fuifehf ef hw wf", null));
        mDataset.add(new AlMaghribNewsModelContainer(null, "QShams wins Liwa Cup", "Admin", "ef sf fuifehf ef hw wf", "dummyurl"));
        mDataset.add(new AlMaghribNewsModelContainer(null, "AlMaghrib launches in Jamaica", "Admin", "ef sf fuifehf ef hw wf", null));

        mAdapter = new RecyclerViewMaterialAdapter(
                new NewsCardAdapter(getActivity(), mDataset));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        return layoutView;
    }
}
