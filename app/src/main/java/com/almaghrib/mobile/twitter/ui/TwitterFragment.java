package com.almaghrib.mobile.twitter.ui;

import android.content.Context;
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
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.twitter.data.Twitter;
import com.almaghrib.mobile.twitter.data.TwitterApiUriRequestBuilder;
import com.almaghrib.mobile.twitter.jsonModels.Authenticated;
import com.almaghrib.mobile.twitter.jsonModels.Tweet;
import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.util.view.PaginatingListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class TwitterFragment extends Fragment implements
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = TwitterFragment.class.getSimpleName();

    private PaginatingListView listView;
    private TwitterListAdapter listAdapter;
    private Spinner mSpinner;

    private Twitter mTwitter;
    private CharSequence[] mTwitterIds;

    private int mPrevFeedPos = 0;
    private int mCurrFeedPos = 0;

    private boolean mIsUserScrolling = false;
    private boolean isLoading;
    private String mAccessToken;

    public static TwitterFragment init() {
        final TwitterFragment fragment = new TwitterFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "Twitter"; //TODO: use strings
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.twitter_page, container, false);

        listView = (PaginatingListView) layoutView.findViewById(R.id.videosListView);
        listView.setOnScrollListener(this);

        mTwitter = new Twitter();
        listAdapter = new TwitterListAdapter(getActivity(), mTwitter);
        listView.setAdapter(listAdapter);

        mSpinner = (Spinner) layoutView.findViewById(R.id.getFeedSpinner);
        mSpinner.setOnItemSelectedListener(this);

        // TODO: get id from file matching text on spinner
        mTwitterIds = getActivity().getResources().getTextArray(R.array.twitter_timeline_ids);

//        if (NetworkUtils.isConnectedToNetwork(getActivity().getApplicationContext())) {
//            downloadTweets(mTwitterIds[pos].toString());
//        } else {
//            Toast.makeText(getActivity().getApplicationContext(),
//                    "No Connection", Toast.LENGTH_SHORT).show();
//        }

        return layoutView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (!mTwitter.isEmpty()) {
            // Need to allow first time user enters page so check library for null
            // Prevent users from choosing another channel and triggering a new
            // request until previous/current one finishes
            mSpinner.setEnabled(false);
        }
        mCurrFeedPos = pos;

        getActivity().setProgressBarIndeterminateVisibility(true);
        // if user has tried to get new page but then chose new item - remove loading footer view
        listView.removeLoadingFooterView();

        //getUserYouTubeFeed(getActivity().getApplicationContext(), true);
        downloadTweets(mTwitterIds[pos].toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // download twitter timeline after first checking to see if there is a network connection
    public void downloadTweets(String screenName) {
        final Context appContext = getActivity().getApplicationContext();

        // Stop all other requests, e.g. to get new page
        RequestQueueSingleton.getInstance(appContext).cancelPendingRequests(TAG);

        final TwitterApiUriRequestBuilder apiBuilder = new TwitterApiUriRequestBuilder();

        final GsonRequest<Authenticated> request =
                new GsonRequest<Authenticated>(
                        Request.Method.POST,
                        apiBuilder.buildTokenRequest(),
                        Authenticated.class, null,
                        apiBuilder.buildTokenRequestHeaders(),
                        apiBuilder.getTokenRequestEntity(),
                        createSearchRequestAuthenticateSuccessListener(screenName),
                        createSearchRequestErrorListener());

        request.setTag(TAG);
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(request);
    }

    private Response.Listener<Authenticated> createSearchRequestAuthenticateSuccessListener(final String screenName) {
        return new Response.Listener<Authenticated>() {
            @Override
            public void onResponse(Authenticated response) {
                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (response != null && response.getTokenType().equals("bearer")) {
                    mAccessToken = response.getAccessToken();
                    makeTwitterTimelineRequest(response.getAccessToken(), screenName);
                } else {
                    Log.e(TAG, "Invalid token: " + response);
                    mSpinner.setSelection(mPrevFeedPos);
                    mCurrFeedPos = mPrevFeedPos;
                    mSpinner.setEnabled(true);
                }
                getActivity().setProgressBarIndeterminateVisibility(false);
                isLoading = false;
                listView.removeLoadingFooterView();
            }
        };
    }

    private void makeTwitterTimelineRequest(String accessToken, String screenName) {
        final Context appContext = getActivity().getApplicationContext();

        // Stop all other requests, e.g. to get new page
        RequestQueueSingleton.getInstance(appContext).cancelPendingRequests(TAG);

        final TwitterApiUriRequestBuilder apiBuilder = new TwitterApiUriRequestBuilder();

        final String maxId = (!mTwitter.isEmpty() && screenName.equals(mTwitter.getScreenName()))
                ? mTwitter.get(mTwitter.size()-1).getId() : null;

        // update the results with the body of the response
        final GsonRequest<Twitter> request =
                new GsonRequest<Twitter>(
                        Request.Method.GET,
                        apiBuilder.buildStatusUserTimelineRequest(screenName, maxId),
                        Twitter.class, null,
                        apiBuilder.buildApiRequestHeaders(accessToken), null,
                        createSearchRequestSuccessListener(screenName),
                        createSearchRequestErrorListener());

        request.setTag(TAG);
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(request);
    }

    private Response.ErrorListener createSearchRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                mSpinner.setSelection(mPrevFeedPos);
                mCurrFeedPos = mPrevFeedPos;
                mSpinner.setEnabled(true);
                getActivity().setProgressBarIndeterminateVisibility(false);
                isLoading = false;
                listView.removeLoadingFooterView();
            }
        };
    }

    private Response.Listener<Twitter> createSearchRequestSuccessListener(final String screenName) {
        return new Response.Listener<Twitter>() {
            @Override
            public void onResponse(Twitter response) {
                if (response != null && !response.isEmpty()) {
                    mPrevFeedPos = mCurrFeedPos;
                    if (screenName.equals(mTwitter.getScreenName())) {
                        mTwitter.addAll(response);
                    } else {
                        mTwitter = response;
                        mTwitter.setScreenName(screenName);
                    }
                    // lets write the results to the console as well
                    for (Tweet tweet : response) {
                        Log.d(TAG, tweet.toString());
                    }
                    // send the tweets to the adapter for rendering
                    listAdapter.updateAdapter(mTwitter);
                }
                getActivity().setProgressBarIndeterminateVisibility(false);
                isLoading = false;
                listView.removeLoadingFooterView();
                mSpinner.setEnabled(true);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setProgressBarIndeterminateVisibility(false);
        // Only cancel requests from this fragment
        RequestQueueSingleton.getInstance(getActivity().getApplicationContext())
                .cancelPendingRequests(TAG);
        mSpinner.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        listView.destroyDrawingCache();
        listView = null;
        listAdapter.notifyDataSetInvalidated();
        listAdapter = null;
        mSpinner = null;
        mTwitter = null;

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
        if (listAdapter != null && !listAdapter.isEmpty() && listAdapter.getCount() < 250) { // Prevent user from making too many requests
            // If the first page is not full, or the user is scrolling and they are
            // less than one page from the end of the list, then load more items
            final int lastVisibleItem = firstVisibleItem + visibleItemCount;
            final boolean pageNotFull = visibleItemCount == totalItemCount;
            final boolean onePageFromEnd = lastVisibleItem + visibleItemCount > totalItemCount;
            if (!isLoading && (pageNotFull || (mIsUserScrolling && onePageFromEnd))) {
                isLoading = true;
                listView.addLoadingFooterView();
                makeTwitterTimelineRequest(mAccessToken, mTwitterIds[mCurrFeedPos].toString());
            }
        }
    }

}
