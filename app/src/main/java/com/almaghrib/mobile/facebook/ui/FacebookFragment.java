package com.almaghrib.mobile.facebook.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.facebook.data.FacebookApiConstants;
import com.almaghrib.mobile.facebook.data.FacebookFeedItem;
import com.almaghrib.mobile.facebook.jsonModels.FacebookFeedDataModel;
import com.almaghrib.mobile.facebook.jsonModels.FacebookFeedModelContainer;
import com.almaghrib.mobile.facebook.jsonModels.FacebookFeedPagingModel;
import com.almaghrib.mobile.util.DateUtils;
import com.almaghrib.mobile.util.view.PaginatingListView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;

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

    private CallbackManager mCallbackManager;
    private GraphRequest mNextPageRequest;
    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "onSuccess");
            setupSignedInViews(getView());
        }
        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }
        @Override
        public void onError(FacebookException e) {
            Log.e(TAG, "onError " + e.getMessage(), e);
        }
    };

    private PaginatingListView listView;
    private FacebookFeedListAdapter listAdapter;
    private Spinner mSpinner;

    private List<FacebookFeedItem> feedItems;

    private CharSequence[] mFeedIds;
    private CharSequence[] mFeedTitles;

    private int mPrevFeedPos = 0;
    private int mCurrFeedPos = 0;
    private boolean mIsUserScrolling = false;
    private boolean isLoading;

    public static FacebookFragment init() {
        final FacebookFragment fragment = new FacebookFragment();
        return fragment;
    }

    public static String getFragmentName() {
        return "Facebook"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.facebook_page, container, false);
        // Set up views depending on whether user is signed in or not
        if (AccessToken.getCurrentAccessToken() != null) {
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

    private void setupSignedInViews(View view) {
        // hide login button
        final LoginButton buttonLogin = (LoginButton) view.findViewById(R.id.login_button);
        buttonLogin.setVisibility(View.GONE);

        final View feedViews = view.findViewById(R.id.feedViews);
        feedViews.setVisibility(View.VISIBLE);

        listView = (PaginatingListView) view.findViewById(R.id.list);
        feedItems = new ArrayList<FacebookFeedItem>();
        listView.setOnScrollListener(this);
        listAdapter = new FacebookFeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);

        mSpinner = (Spinner) view.findViewById(R.id.getFeedSpinner);
        // TODO: get id from file matching text on spinner
        mFeedIds = getActivity().getResources().getTextArray(R.array.facebook_feed_ids);
        mFeedTitles = getActivity().getResources().getTextArray(R.array.facebook_feed_titles);

        mSpinner.setOnItemSelectedListener(this);
    }

    private void setupLoginButton(View view) {
        final LoginButton buttonLogin = (LoginButton) view.findViewById(R.id.login_button);
        if (buttonLogin != null) {
            buttonLogin.setFragment(this);
            buttonLogin.registerCallback(mCallbackManager, mFacebookCallback);
        }
    }

    /**
     * Retrieve a user's feed
     * @param context
     */
    public void getUserFacebookFeed(Context context){
        mPrevFeedPos = mCurrFeedPos;
        feedItems = new ArrayList<FacebookFeedItem>();
        mNextPageRequest = null;
        makeFacebookFeedRequest();
    }

    private void makeFacebookFeedRequest() {
        final Bundle paramsBundle = new Bundle();
        paramsBundle.putString(FacebookApiConstants.FIELDS_PARAM, FacebookApiConstants.FIELDS_VALUE);

        if (mNextPageRequest != null) {
            mNextPageRequest.setParameters(paramsBundle);
            mNextPageRequest.setCallback(getGraphRequestCallback());
            mNextPageRequest.executeAsync();
        } else {
            final String userId = mFeedIds[mCurrFeedPos].toString();
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + userId + FacebookApiConstants.FEED_REQUEST,
                    paramsBundle,
                    HttpMethod.GET,
                    getGraphRequestCallback()
            ).executeAsync();
        }
    }

    private GraphRequest.Callback getGraphRequestCallback() {
        return new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                if (response != null && response.getError() == null) { // no errors
                    handleSuccessfulFeedRequest(response);
                } else { // handle error
                    handleErrorFeedRequest(response);
                }
                final Activity activity = getActivity();
                if (activity != null) {
                    activity.setProgressBarIndeterminateVisibility(false);
                }
                isLoading = false;
                listView.removeLoadingFooterView();
                mSpinner.setEnabled(true);
            }
        };
    }

    private void handleSuccessfulFeedRequest(@NonNull GraphResponse response) {
        Log.d(TAG, "Complete:" + response.getRawResponse());
        final Gson gson = new Gson();
        final FacebookFeedModelContainer model = gson.fromJson(
                response.getRawResponse(), FacebookFeedModelContainer.class);
        final FacebookFeedPagingModel pagingInfo = model.getPagingInfo();
        mNextPageRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        populateListWithFeed(model.getData());
    }

    private void handleErrorFeedRequest(GraphResponse response) {
        if (response != null) {
            Log.e(TAG, "Error:" + response.getError());
            if (response.getError() != null) {
                Log.e(TAG, response.getError().getErrorMessage(), response.getError().getException());
            }
        } else {
            Log.e(TAG, "Error: null response");
        }
        mNextPageRequest = null;
        mSpinner.setSelection(mPrevFeedPos);
        mCurrFeedPos = mPrevFeedPos;
    }

    /**
     * This method retrieves the Library of videos from the retrieved items and
     * passes them to our ListView
     * @param items list of feed items
     */
    private void populateListWithFeed(ArrayList<FacebookFeedDataModel> items) {
        if (items != null) {
            final String currentUserFeed = mFeedTitles[mCurrFeedPos].toString();

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
                                pictureUrl, itemsModel.getMessage(), formattedTime,
                                itemsModel.getLink()));
                    }
                }
            }
            final Activity activity = getActivity();
            if (activity != null) {
                activity.setProgressBarIndeterminateVisibility(false);
            }
            listAdapter.setUserId(mFeedIds[mCurrFeedPos].toString());
            listAdapter.updateAdapter(feedItems);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setProgressBarIndeterminateVisibility(false);
        // Only cancel requests from this fragment
        RequestQueueSingleton.getInstance(getActivity().getApplicationContext())
                .cancelPendingRequests(TAG);
        if (mSpinner != null) {
            mSpinner.setEnabled(true);
        }
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
            mSpinner = null;
            feedItems = null;
        }
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
        if (mNextPageRequest != null) {
            // If the first page is not full, or the user is scrolling and they are
            // less than one page from the end of the list, then load more items
            final int lastVisibleItem = firstVisibleItem + visibleItemCount;
            final boolean pageNotFull = visibleItemCount == totalItemCount;
            final boolean onePageFromEnd = lastVisibleItem + visibleItemCount > totalItemCount;
            if(!isLoading && (pageNotFull || (mIsUserScrolling && onePageFromEnd))) {
                isLoading = true;
                listView.addLoadingFooterView();
                makeFacebookFeedRequest();
            }
        }
    }

}