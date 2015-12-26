package com.almaghrib.mobile;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribNewsModelContainer;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;

public class NewsArticleFragment extends Fragment {
    private static final String BUNDLE_IMAGE_TRANSITION_NAME_TAG = "IMAGE_TRANSITION_NAME";
    private static final String BUNDLE_MODEL_TAG = "MODEL";

    private ImageLoader mImageLoader;

    public static NewsArticleFragment init(String imageTransitionName, AlMaghribNewsModelContainer model) {
        final NewsArticleFragment fragment = new NewsArticleFragment();
        final Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_IMAGE_TRANSITION_NAME_TAG, imageTransitionName);
        bundle.putSerializable(BUNDLE_MODEL_TAG, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getFragmentName() {
        return "News article"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = RequestQueueSingleton.getInstance(getActivity().getApplicationContext()).getImageLoader();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.news_article_page, container, false);

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

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) layoutView.findViewById(R.id.collapsing_container);
        collapsingToolbar.setTitle("News");

        populateUI(layoutView);

        return layoutView;
    }

    private void populateUI(View layoutView) {
        final Bundle bundle = getArguments();
        if (bundle != null) {
            final AlMaghribNewsModelContainer model = (AlMaghribNewsModelContainer) bundle.getSerializable(BUNDLE_MODEL_TAG);

            if (model != null) {
                final FeedImageView bannerImageView = (FeedImageView) layoutView.findViewById(R.id.seminarBannerImageView);
                bannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
                //bannerImageView.setImageUrl(model.getBannerUrl(), mImageLoader);
                ViewCompat.setTransitionName(bannerImageView, bundle.getString(BUNDLE_IMAGE_TRANSITION_NAME_TAG));

                final TextView headerTextView = (TextView) layoutView.findViewById(R.id.titleTextView);
                headerTextView.setText(model.getTitle());
                final TextView postedByTextView = (TextView) layoutView.findViewById(R.id.postedByTextView);
                postedByTextView.setText("Posted by: " + model.getAuthor());
                final TextView newsBodyTextView = (TextView) layoutView.findViewById(R.id.newsBodyTextView);
                newsBodyTextView.setText(model.getBodyContent());

                final FloatingActionButton fab = (FloatingActionButton) layoutView.findViewById(R.id.fabPlay);
                if (model.getVideoUrl() != null) {
                    // TODO: do play task

                } else {
                    final CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                    p.setAnchorId(View.NO_ID);
                    fab.setLayoutParams(p);
                    fab.setVisibility(View.GONE);
                }
            }

        }
    }

}
