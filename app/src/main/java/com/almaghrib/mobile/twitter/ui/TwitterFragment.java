package com.almaghrib.mobile.twitter.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.almaghrib.mobile.AlMaghribSharedPreferences;
import com.almaghrib.mobile.R;
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.twitter.data.Twitter;
import com.almaghrib.mobile.twitter.data.TwitterApiUriRequestBuilder;
import com.almaghrib.mobile.twitter.jsonModels.Tweet;
import com.almaghrib.mobile.util.NetworkUtils;
import com.almaghrib.mobile.util.view.PaginatingListView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class TwitterFragment extends Fragment implements
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener,
        TwitterSignInDialogFragment.AuthorizationCompleteCallback {

    private static final String TAG = TwitterFragment.class.getSimpleName();

    private OAuthService mService;

    private PaginatingListView listView;
    private TwitterListAdapter listAdapter;
    private Spinner mSpinner;

    private ImageButton mSignInButton;

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
        mService = TwitterApiUriRequestBuilder.buildOAuthService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();

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

        mSignInButton = (ImageButton) layoutView.findViewById(R.id.twitterSignInButton);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInToTwitter(context);
            }
        });
        final AlMaghribSharedPreferences prefs = AlMaghribSharedPreferences.getInstance(context);
        if (prefs.getTwitterAccessToken().isEmpty()) {
            mSignInButton.setVisibility(View.VISIBLE);
        }

        return layoutView;
    }

    private void signInToTwitter(Context context) {
        if (NetworkUtils.isConnectedToNetwork(context)) {
            TwitterSignInDialogFragment.showTwitterSignInDialog(
                    getFragmentManager(), this, mService);
        } else {
            Toast.makeText(context, getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
        }
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

        // if user has tried to get new page but then chose new item - remove loading footer view
        listView.removeLoadingFooterView();

        final AlMaghribSharedPreferences prefs =
                AlMaghribSharedPreferences.getInstance(getActivity().getApplicationContext());
        if (!prefs.getTwitterAccessToken().isEmpty()) {
            getActivity().setProgressBarIndeterminateVisibility(true);
            makeTwitterTimelineRequest(mTwitterIds[pos].toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onAuthorizationComplete(Token requestToken, String url) {
        new GetAccessTokenTask(mService, requestToken).execute(url);
    }

    private class GetAccessTokenTask extends AsyncTask<String, Void, Boolean> {
        private OAuthService service;
        private Token requestToken;
        private AlMaghribSharedPreferences prefs;
        private ProgressDialog progressDialog ;

        public GetAccessTokenTask(OAuthService service, Token requestToken) {
            this.service = service;
            this.requestToken = requestToken;
            this.prefs = AlMaghribSharedPreferences.getInstance(getActivity().getApplicationContext());
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading_text));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... param) {
            final Uri uri = Uri.parse(param[0]);
            final String verifier = uri.getQueryParameter("oauth_verifier");
            final Verifier v = new Verifier(verifier);
            try {
                final Token accessToken = service.getAccessToken(requestToken, v);
                Log.d(TAG, "access token - Raw response: " + accessToken.getRawResponse());
                // The requestToken is saved for use later on to verify the OAuth request.
                prefs.setTwitterAccessToken(accessToken.getToken());
                prefs.setTwitterAccessTokenSecret(accessToken.getSecret());

                return true;
            } catch (OAuthConnectionException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return false;
        }
        @Override
        protected void onCancelled(Boolean aVoid) {
            progressDialog.dismiss();
            super.onCancelled(aVoid);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (getView() != null && result) {
                mSignInButton.setVisibility(View.GONE);
                makeTwitterTimelineRequest(mTwitterIds[mCurrFeedPos].toString());
                progressDialog.dismiss();
            }
        }
    }

    private void makeTwitterTimelineRequest(String screenName) {
        new MakeTimelineRequestTask(screenName).execute();
    }

    private class MakeTimelineRequestTask extends AsyncTask<Void, Void, Twitter> {
        private String mScreenName;
        private AlMaghribSharedPreferences mPrefs;

        public MakeTimelineRequestTask(String screenName) {
            mScreenName = screenName;
            mPrefs = AlMaghribSharedPreferences.getInstance(getActivity().getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // some loading indicator
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Twitter doInBackground(Void... param) {
            final String maxId = (!mTwitter.isEmpty() && mScreenName.equals(mTwitter.getScreenName()))
                    ? mTwitter.get(mTwitter.size()-1).getId() : null;

            final OAuthRequest request =
                    TwitterApiUriRequestBuilder.buildStatusUserTimelineRequest(mScreenName, maxId);

            final Token accessToken = new Token(
                    mPrefs.getTwitterAccessToken(), mPrefs.getTwitterAccessTokenSecret());

            mService.signRequest(accessToken, request);
            try {
                org.scribe.model.Response response = request.send();
                if (response.isSuccessful()) {
                    Log.d(TAG, "response: " + response.getBody());
                    try {
                        final Twitter twitterJson = new Gson().fromJson(response.getBody(), Twitter.class);
                        return twitterJson;
                    } catch (JsonSyntaxException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                Log.d(TAG, "response status: " + response.getMessage());
            } catch (OAuthConnectionException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null; // request not successful
        }
        @Override
        protected void onPostExecute(Twitter response) {
            if (getView() != null) {
                // update UI
                if (response != null && !response.isEmpty()) {
                    mPrevFeedPos = mCurrFeedPos;
                    if (mScreenName.equals(mTwitter.getScreenName())) {
                        mTwitter.addAll(response);
                    } else {
                        // new feed channel
                        mTwitter = response;
                        mTwitter.setScreenName(mScreenName);
                    }
                    // lets write the results to the console as well
                    for (Tweet tweet : response) {
                        Log.d(TAG, tweet.toString());
                    }
                    // send the tweets to the adapter for rendering
                    listAdapter.updateAdapter(mTwitter);
                } else {
                    Log.e(TAG, "Invalid response: " + response);
                    mSpinner.setSelection(mPrevFeedPos);
                    mCurrFeedPos = mPrevFeedPos;
                    mSpinner.setEnabled(true);

                    //TODO: Show retry button
                }
                getActivity().setProgressBarIndeterminateVisibility(false);
                isLoading = false;
                listView.removeLoadingFooterView();
                mSpinner.setEnabled(true);
            }
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
                makeTwitterTimelineRequest(mTwitterIds[mCurrFeedPos].toString());
            }
        }
    }

}
