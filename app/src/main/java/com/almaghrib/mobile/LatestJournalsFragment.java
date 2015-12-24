package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribJournalModelContainer;
import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarBannerModel;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;
import java.util.List;

public class LatestJournalsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<AlMaghribJournalModelContainer> mDataset;

    public static LatestJournalsFragment init() {
        final LatestJournalsFragment fragment = new LatestJournalsFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "Journals"; //TODO: use strings
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

        mDataset = new ArrayList<AlMaghribJournalModelContainer>();

        // TODO : remove dummy data
        mDataset.add(new AlMaghribJournalModelContainer(null, "AlMaghrib launches in Australia"));
        mDataset.add(new AlMaghribJournalModelContainer(null, "Global Impact Day makes an entrance"));
        mDataset.add(new AlMaghribJournalModelContainer(null, "QShams wins Liwa Cup"));
        mDataset.add(new AlMaghribJournalModelContainer(null, "AlMaghrib launches in Jamaica"));

        mAdapter = new RecyclerViewMaterialAdapter(
                new LatestJournalCardAdapter(getActivity(), mDataset));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        return layoutView;
    }
}
