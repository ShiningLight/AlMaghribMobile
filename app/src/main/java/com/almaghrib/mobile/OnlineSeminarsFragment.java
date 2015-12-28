package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribHomeSeminarModelContainer;
import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarBannerModel;
import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarsLocationListModelContainer;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnlineSeminarsFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<AlMaghribUpcomingSeminarBannerModel> mDataset;

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
        final View layoutView = inflater.inflate(R.layout.seminars_list_page, container, false);

        //populateHeaderCard(layoutView);

        mRecyclerView = (RecyclerView) layoutView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDataset = new ArrayList<AlMaghribUpcomingSeminarBannerModel>();

        // TODO : remove dummy data
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"https://scontent.cdninstagram.com/hphotos-xap1/t51.2885-15/s480x480/e35/11939401_1389735914666697_1538854467_n.jpg",
                "London", "Love Notes", "Marriage & Family Life", "Shaykh Yahya Ibrahim", "Jan 8-10", "Queen Mary University, Whitechapel Campus, Perrin Lecture Theatre, London, E1 2AT"));
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"http://i.ytimg.com/vi/qhypWutXRqo/hqdefault.jpg",
                "Toronto", "IlmFest 2016", "Islamic Conference", "Multiple instructors", "Jan 8-10", "Queen Mary University, Whitechapel Campus, Perrin Lecture Theatre, London, E1 2AT"));
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"https://fbcdn-photos-d-a.akamaihd.net/hphotos-ak-xal1/v/t1.0-0/s480x480/12088289_961360220603205_3150621638343165157_n.jpg?oh=b5b04443914eb87cc07fc433382d790b&oe=56CC5CCA&__gda__=1456602297_5a7f033b68d547318d1bd28aa6379d63",
                "Toronto", "World of Dreams", "Open the realm of dreams", "Shaykh Waleed Basyouni", "Jan 1-10", "Queen Mary University, Whitechapel Campus, Perrin Lecture Theatre, London, E1 2AT"));
        mDataset.add(new AlMaghribUpcomingSeminarBannerModel(null,//"http://i1.imgiz.com/rshots/8818/islam-channelden-asad-lath-a9-icin-ne-dedi_8818494-6980_640x360.jpg",
                "London","World of Funnnn", "Fun & Things", "Shaykh Asad Lath", "Feb 1-10", "Queen Mary University, Whitechapel Campus, Perrin Lecture Theatre, London, E1 2AT"));

        final List<String> cities = new ArrayList<String>();
        cities.add("London");
        cities.add("Toronto");

        final UpcomingSeminarCardAdapter upcomingSeminarCardAdapter = new UpcomingSeminarCardAdapter(getActivity(), mDataset);
        upcomingSeminarCardAdapter.setHeader(new AlMaghribUpcomingSeminarsLocationListModelContainer("", "Select your country", cities));
        mAdapter = new RecyclerViewMaterialAdapter(upcomingSeminarCardAdapter);

        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        return layoutView;
    }

    private void populateHeaderCard(View layoutView) {
        final FeedImageView bannerImageView = (FeedImageView) layoutView.findViewById(R.id.seminarBannerImageView);
        bannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
        //bannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);

        final TextView titleTextView = (TextView) layoutView.findViewById(R.id.titleTextView);
        titleTextView.setText("Select Your Country");

        final Spinner spinner = (Spinner) layoutView.findViewById(R.id.getFeedSpinner);
    }
}
