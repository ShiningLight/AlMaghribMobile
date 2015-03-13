package com.almaghrib.mobile.facebook.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.facebook.data.FacebookApiUriRequestBuilder;
import com.almaghrib.mobile.facebook.data.FacebookFeedItem;
import com.almaghrib.mobile.facebook.jsonModels.FacebookFeedDataModel;
import com.almaghrib.mobile.facebook.jsonModels.FacebookFeedModelContainer;
import com.almaghrib.mobile.facebook.jsonModels.FacebookFeedPagingModel;
import com.almaghrib.mobile.util.DateUtils;
import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.util.view.PaginatingListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FacebookFragment extends Fragment implements
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {

    protected final static String TAG = FacebookFragment.class.getSimpleName();

    private static final SimpleDateFormat RETRIEVED_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT =
            new SimpleDateFormat("d MMM yyyy 'at' HH:mm");

    private int fragVal;

    private PaginatingListView listView;
    private FacebookFeedListAdapter listAdapter;
    private Spinner mSpinner;

    private List<FacebookFeedItem> feedItems;

    private CharSequence[] mFeedIds;
    private CharSequence[] mFeedTitles;
    private String mNextPageToken;

    private String mAccessToken;
    private int mPrevFeedPos = 0;
    private int mCurrFeedPos = 0;
    private boolean mIsUserScrolling = false;
    private boolean isLoading;

    public static FacebookFragment init(int val) {
        final FacebookFragment fragment = new FacebookFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        fragment.setArguments(args);
        return fragment;
    }


    public static String getFragmentName() {
        return "Facebook"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.facebook_page, container, false);

        listView = (PaginatingListView) layoutView.findViewById(R.id.list);
        feedItems = new ArrayList<FacebookFeedItem>();
        listView.setOnScrollListener(this);
        listAdapter = new FacebookFeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);

        mSpinner = (Spinner) layoutView.findViewById(R.id.getFeedSpinner);
        // TODO: get id from file matching text on spinner
        mFeedIds = getActivity().getResources().getTextArray(R.array.facebook_feed_ids);
        mFeedTitles = getActivity().getResources().getTextArray(R.array.facebook_feed_titles);

        mSpinner.setOnItemSelectedListener(this);

        return layoutView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (feedItems != null && !feedItems.isEmpty()) {
            // Need to allow first time user enters page so check library for null
            // Prevent users from choosing another channel and triggering a new
            // request until previous/current one finishes
            mSpinner.setEnabled(false);
        }

        mCurrFeedPos = pos;

        getActivity().setProgressBarIndeterminateVisibility(true);
        // if user has tried to get new page but then chose new item - remove loading footer view
        listView.removeLoadingFooterView();

        getUserFacebookFeed(getActivity().getApplicationContext());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Retrieve a users video feed
     * @param context
     */
    public void getUserFacebookFeed(Context context){
        final Context appContext = context.getApplicationContext();

        // Stop all other requests, e.g. to get new page
        RequestQueueSingleton.getInstance(appContext).cancelPendingRequests(TAG);

        final String url = new FacebookApiUriRequestBuilder().buildAccessTokenRequest();
        // Get access token request
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPrevFeedPos = mCurrFeedPos;
                        feedItems = new ArrayList<FacebookFeedItem>();
                        mNextPageToken = null;
                        // Retrieved access token
                        mAccessToken = response;
                        makeFacebookFeedRequest(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.e(TAG, e.getMessage(), e);
                        getActivity().setProgressBarIndeterminateVisibility(false);
                        mSpinner.setSelection(mPrevFeedPos);
                        mCurrFeedPos = mPrevFeedPos;
                        mSpinner.setEnabled(true);
                    }
                });

        request.setTag(TAG);
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(request);
    }

    private void makeFacebookFeedRequest(String accessToken) {
        final Context appContext = getActivity().getApplicationContext();

        // Stop all other requests, e.g. to get new page
        RequestQueueSingleton.getInstance(appContext).cancelPendingRequests(TAG);

        final String url;
        if (mNextPageToken != null) {
            url = mNextPageToken;
        } else {
            url = new FacebookApiUriRequestBuilder()
                    .buildFeedRequest(mFeedIds[mCurrFeedPos].toString(), accessToken);
        }

        final GsonRequest<FacebookFeedModelContainer> request =
                new GsonRequest<FacebookFeedModelContainer>(
                        Request.Method.GET,
                        url,
                        FacebookFeedModelContainer.class,
                        createSearchRequestSuccessListener(),
                        createSearchRequestErrorListener());

        request.setTag(TAG);
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(request);
    }

    private Response.Listener<FacebookFeedModelContainer> createSearchRequestSuccessListener() {
        return new Response.Listener<FacebookFeedModelContainer>() {
            @Override
            public void onResponse(FacebookFeedModelContainer response) {
                try {
                    if (response != null) {
                        Log.d(TAG, response.toString());
                        final FacebookFeedPagingModel pagingInfo = response.getPagingInfo();
                        mNextPageToken = (pagingInfo != null) ? pagingInfo.getNext() : null;
                        populateListWithFeed(response.getData());
                    } else {
                        Log.d(TAG, "Response is null");
                        mNextPageToken = null;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    mSpinner.setSelection(mPrevFeedPos);
                    mCurrFeedPos = mPrevFeedPos;
                    // if access token has timed out make another request
                }
                getActivity().setProgressBarIndeterminateVisibility(false);
                isLoading = false;
                listView.removeLoadingFooterView();
                mSpinner.setEnabled(true);
            }
        };
    }

    private Response.ErrorListener createSearchRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                getActivity().setProgressBarIndeterminateVisibility(false);
                isLoading = false;
                listView.removeLoadingFooterView();
                mSpinner.setSelection(mPrevFeedPos);
                mCurrFeedPos = mPrevFeedPos;
                mSpinner.setEnabled(true);
            }
        };
    }

    /**
     * This method retrieves the Library of videos from the retrieved items and
     * passes them to our ListView
     * @param items list of feed items
     */
    private void populateListWithFeed(ArrayList<FacebookFeedDataModel> items) {
        if (items != null) {
            final String currentUserFeed = mFeedTitles[mCurrFeedPos].toString();
            final String profilePicUrl = new FacebookApiUriRequestBuilder()
                    .buildProfilePictureRequest(mFeedIds[mCurrFeedPos].toString());

            for (int i = 0; i < items.size(); i++) {
                final FacebookFeedDataModel itemsModel = items.get(i);

                final String feedUser = itemsModel.getFrom().getName();
                final String story = itemsModel.getStory();
                if (!TextUtils.isEmpty(feedUser) && feedUser.equals(currentUserFeed)) {
                    if (!TextUtils.isEmpty(story) && (
                            story.matches(currentUserFeed + " commented on (.*.)") ||
                                    story.matches(currentUserFeed + " likes a (.*.)"))) {
                        // user has commented on own status, so we already have printed the status post
                        // or user has just liked a photo
                    } else {
                        final String formattedTime = DateUtils.getFormattedDate(
                                RETRIEVED_DATE_FORMAT, OUTPUT_DATE_FORMAT, itemsModel.getCreatedTime());

                        String pictureUrl = null;
                        if (itemsModel.getAttachments() != null &&
                                itemsModel.getAttachments().getData() != null &&
                                !itemsModel.getAttachments().getData().isEmpty() &&
                                itemsModel.getAttachments().getData().get(0) != null &&
                                itemsModel.getAttachments().getData().get(0).getMedia() != null &&
                                itemsModel.getAttachments().getData().get(0).getMedia().getImage() != null) {
                            pictureUrl = itemsModel.getAttachments().getData().get(0)
                                    .getMedia().getImage().getSrc();
                        }
                        // Create the video object and add it to our list
                        feedItems.add(new FacebookFeedItem(itemsModel.getFrom().getName(),
                                pictureUrl, itemsModel.getMessage(), profilePicUrl,
                                formattedTime, itemsModel.getLink()));
                    }
                }
            }
            getActivity().setProgressBarIndeterminateVisibility(false);
            listAdapter.updateAdapter(feedItems);
        }
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
        feedItems = null;

        if (getActivity().getSupportFragmentManager().getFragments().contains(this)) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this);
        }

        super.onDestroy();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch(scrollState) {
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
        if(mNextPageToken != null) {
            // If the first page is not full, or the user is scrolling and they are
            // less than one page from the end of the list, then load more items
            final int lastVisibleItem = firstVisibleItem + visibleItemCount;
            final boolean pageNotFull = visibleItemCount == totalItemCount;
            final boolean onePageFromEnd = lastVisibleItem + visibleItemCount > totalItemCount;
            if(!isLoading && (pageNotFull || (mIsUserScrolling && onePageFromEnd))) {
                isLoading = true;
                listView.addLoadingFooterView();
                makeFacebookFeedRequest(null);
            }
        }
    }

}