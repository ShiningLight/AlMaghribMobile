package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarBannerModel;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeminarsFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<AlMaghribUpcomingSeminarBannerModel> mDataset;

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

        mDataset = new ArrayList<AlMaghribUpcomingSeminarBannerModel>();

        // TODO : remove dummy data
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"https://scontent.cdninstagram.com/hphotos-xap1/t51.2885-15/s480x480/e35/11939401_1389735914666697_1538854467_n.jpg",
                "London", "Love Notes", "Shaykh Yahya Ibrahim", "January 8-10", "Queen Mary University"));
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"http://i.ytimg.com/vi/qhypWutXRqo/hqdefault.jpg",
                "Toronto", "IlmFest 2016", "Multiple instructors", "January 8-10", "University of Toronto"));
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"https://fbcdn-photos-d-a.akamaihd.net/hphotos-ak-xal1/v/t1.0-0/s480x480/12088289_961360220603205_3150621638343165157_n.jpg?oh=b5b04443914eb87cc07fc433382d790b&oe=56CC5CCA&__gda__=1456602297_5a7f033b68d547318d1bd28aa6379d63",
                "Toronto", "World of Dreams", "Shaykh Waleed Basyouni", "January 1-10", "University of Egypt"));
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"http://i1.imgiz.com/rshots/8818/islam-channelden-asad-lath-a9-icin-ne-dedi_8818494-6980_640x360.jpg",
                "London","World of Funnnn", "Shaykh Asad Lath", "February 1-10", "Regents Park Hall"));

        mAdapter = new RecyclerViewMaterialAdapter(
                new SeminarCardAdapter(getActivity(), mDataset));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        return layoutView;
    }
}
