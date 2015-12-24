package com.almaghrib.mobile;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;

public class SeminarInfoFragment extends Fragment {

    private ImageLoader mImageLoader;

    public static SeminarInfoFragment init(String imageTransitionName) {
        final SeminarInfoFragment fragment = new SeminarInfoFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("TRANS_NAME", imageTransitionName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getFragmentName() {
        return "Seminars Info"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = RequestQueueSingleton.getInstance(getActivity().getApplicationContext()).getImageLoader();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.seminar_info_page, container, false);

        final FeedImageView bannerImageView = (FeedImageView) layoutView.findViewById(R.id.seminarBannerImageView);
        bannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
        //bannerImageView.setImageUrl("https://scontent.cdninstagram.com/hphotos-xap1/t51.2885-15/s480x480/e35/11939401_1389735914666697_1538854467_n.jpg", mImageLoader);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            String transitionName = bundle.getString("TRANS_NAME");
            ViewCompat.setTransitionName(bannerImageView, transitionName);
        }

        final Toolbar toolbar = (Toolbar) layoutView.findViewById(R.id.technique_three_toolbar);
        final AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.getSupportActionBar().hide();
        parentActivity.setSupportActionBar(toolbar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) layoutView.findViewById(R.id.collapsing_container);
        collapsingToolbar.setTitle("Love Notes");

        return layoutView;
    }

}
