package com.almaghrib.mobile.twitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.twitter.data.Twitter;
import com.almaghrib.mobile.twitter.data.TwitterApiUriRequestBuilder;
import com.almaghrib.mobile.util.view.PaginatingListView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.scribe.oauth.OAuthService;

import io.fabric.sdk.android.Fabric;

public class TwitterFragment extends Fragment implements
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = TwitterFragment.class.getSimpleName();

    private Callback<TwitterSession> mTwitterCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            // Do something with result, which provides a TwitterSession for making API calls
            Log.d(TAG, "onSuccess");
            setupSignedInViews(getView());
        }
        @Override
        public void failure(TwitterException e) {
            Log.e(TAG, "onError " + e.getMessage(), e);
        }
    };

    private PaginatingListView listView;
    private TwitterListAdapter listAdapter;
    private Spinner mSpinner;

    private TwitterLoginButton mLoginButton;

    private Twitter mTwitter;
    private CharSequence[] mTwitterIds;

    private int mPrevFeedPos = 0;
    private int mCurrFeedPos = 0;

    private boolean mIsUserScrolling = false;
    private boolean isLoading;

    public static TwitterFragment init() {
        final TwitterFragment fragment = new TwitterFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "Twitter"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        final TwitterAuthConfig authConfig = new TwitterAuthConfig(
                TwitterApiUriRequestBuilder.CONSUMER_KEY,
                TwitterApiUriRequestBuilder.CONSUMER_SECRET);

        Fabric.with(getActivity().getApplicationContext(),
                new com.twitter.sdk.android.Twitter(authConfig));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.twitter_page, container, false);

        // Set up views depending on whether user is signed in or not
        final SessionManager sessionManager = com.twitter.sdk.android.Twitter.getSessionManager();
        if (sessionManager != null && sessionManager.getActiveSession() != null &&
                sessionManager.getActiveSession().getAuthToken() != null) {
            // user has signed in
            setupSignedInViews(layoutView);

        } else {
            // user has not signed in yet so hide other views
            final View feedViews = layoutView.findViewById(R.id.feedViews);
            feedViews.setVisibility(View.GONE);
            setupLoginButton(layoutView);
        }
        return layoutView;
    }

    private void setupSignedInViews(final View view) {
        // hide login button
        mLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        mLoginButton.setVisibility(View.GONE);

        final View feedViews = view.findViewById(R.id.feedViews);
        feedViews.setVisibility(View.VISIBLE);

        listView = (PaginatingListView) view.findViewById(R.id.videosListView);
        listView.setOnScrollListener(this);

        mTwitter = new Twitter();
        listAdapter = new TwitterListAdapter(getActivity(), mTwitter);
        listView.setAdapter(listAdapter);

        mSpinner = (Spinner) view.findViewById(R.id.getFeedSpinner);
        mSpinner.setOnItemSelectedListener(this);

        // TODO: get id from file matching text on spinner
        mTwitterIds = getActivity().getResources().getTextArray(R.array.twitter_timeline_ids);

    }

    private void setupLoginButton(View view) {
        mLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        if (mLoginButton != null) {
            mLoginButton.setCallback(mTwitterCallback);
            mLoginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        getActivity().setProgressBarIndeterminateVisibility(true);

        mCurrFeedPos = pos;

        // if user has tried to get new page but then chose new item - remove loading footer view
        listView.removeLoadingFooterView();

        makeTwitterTimelineRequest(mTwitterIds[pos].toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void makeTwitterTimelineRequest(String screenName) {

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(screenName)
                .build();
        final TweetTimelineListAdapter adapter =
                new TweetTimelineListAdapter.Builder(getActivity())
                        .setTimeline(userTimeline)
                        .setOnActionCallback(new Callback<com.twitter.sdk.android.core.models.Tweet>() {
                            @Override
                            public void success(Result<com.twitter.sdk.android.core.models.Tweet> result) {
                                getActivity().setProgressBarIndeterminateVisibility(false);
                                isLoading = false;
                                listView.removeLoadingFooterView();
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Log.e(TAG, e.getMessage(), e);

                                mSpinner.setSelection(mPrevFeedPos);
                                mCurrFeedPos = mPrevFeedPos;

                                getActivity().setProgressBarIndeterminateVisibility(false);
                                isLoading = false;
                                listView.removeLoadingFooterView();
                            }
                        })
                        .build();

        listView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {
            if (listView != null) {
                listView.destroyDrawingCache();
                listView = null;
            }
            if (listAdapter != null) {
                listAdapter.notifyDataSetInvalidated();
                listAdapter = null;
            }
        }
        if (getActivity().getSupportFragmentManager().getFragments().contains(this)) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this);
        }

        super.onDestroy();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
            case SCROLL_STATE_TOUCH_SCROLL:
                mIsUserScrolling = true;
                break;
            case SCROLL_STATE_IDLE:
            default:
                mIsUserScrolling = false;
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isLoading && mIsUserScrolling) {
            isLoading = true;
            listView.addLoadingFooterView();
        }
    }

}
