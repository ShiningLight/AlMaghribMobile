package com.almaghrib.mobile.youtube.ui;

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
import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.util.view.PaginatingListView;
import com.almaghrib.mobile.youtube.data.YouTubeApiUriRequestBuilder;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchItemSnippetModel;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchModelContainer;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchResultItemsModel;
import com.almaghrib.mobile.youtube.data.YouTubeVideo;
import com.almaghrib.mobile.youtube.data.YouTubeVideoLibrary;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YouTubeFragment extends Fragment implements
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {

    protected final static String TAG = YouTubeFragment.class.getSimpleName();

    private static final SimpleDateFormat RETRIEVED_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT =
            new SimpleDateFormat("dd-MM-yyyy");

    private int fragVal;

    private PaginatingListView listView;
    private YouTubeVideoAdapter mYouTubeVideoAdapter;
    private Spinner mSpinner;

    private CharSequence[] mChannelIds;
    private String mNextPageToken;
    private YouTubeVideoLibrary mLibrary;

    private int mPrevChannelPos = 0;
    private int mCurrChannelPos = 0;
    private boolean mIsUserScrolling = false;
    private boolean isLoading;

    public static YouTubeFragment init(int val) {
        YouTubeFragment truitonFrag = new YouTubeFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    public static String getFragmentName() {
        return "YouTube"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.youtube_page, container, false);

        listView = (PaginatingListView) layoutView.findViewById(R.id.videosListView);
        listView.setOnScrollListener(this);
        mYouTubeVideoAdapter = new YouTubeVideoAdapter(getActivity(), new ArrayList<YouTubeVideo>());
        listView.setAdapter(mYouTubeVideoAdapter);

        mSpinner = (Spinner) layoutView.findViewById(R.id.getFeedSpinner);
        mSpinner.setOnItemSelectedListener(this);

        // TODO: get id from file matching text on spinner
        mChannelIds = getActivity().getResources().getTextArray(R.array.channel_ids);

        return layoutView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (mLibrary != null) {
            // Need to allow first time user enters page so check library for null
            // Prevent users from choosing another channel and triggering a new
            // request until previous/current one finishes
            mSpinner.setEnabled(false);
        }
        mPrevChannelPos = mCurrChannelPos;
        mCurrChannelPos = pos;

        getActivity().setProgressBarIndeterminateVisibility(true);
        // Create a library to hold our videos
        mLibrary = new YouTubeVideoLibrary(
                mChannelIds[mCurrChannelPos].toString(), new ArrayList<YouTubeVideo>());
        mNextPageToken = null;

        getUserYouTubeFeed(getActivity().getApplicationContext());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Retrieve a users video feed
     * @param context
     */
    public void getUserYouTubeFeed(Context context){
        final String url = new YouTubeApiUriRequestBuilder().buildSearchRequest(
                mChannelIds[mCurrChannelPos].toString(), mNextPageToken);

        final GsonRequest<YouTubeSearchModelContainer> request =
                new GsonRequest<YouTubeSearchModelContainer>(
                        Request.Method.GET,
                        url,
                        YouTubeSearchModelContainer.class,
                        //params,
                        createSearchRequestSuccessListener(),
                        createSearchRequestErrorListener());
        request.setTag(TAG);
        RequestQueueSingleton.getInstance(context.getApplicationContext())
                .addToRequestQueue(request);
    }

    private Response.Listener<YouTubeSearchModelContainer> createSearchRequestSuccessListener() {
        return new Response.Listener<YouTubeSearchModelContainer>() {
            @Override
            public void onResponse(YouTubeSearchModelContainer response) {
                try {
                    Log.d(TAG, response.toString());
                    mNextPageToken = response.getNextPageToken();
                    populateListWithVideos(response.getItems());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    getActivity().setProgressBarIndeterminateVisibility(false);
                    mSpinner.setSelection(mPrevChannelPos);
                    mCurrChannelPos = mPrevChannelPos;
                }
                isLoading = false;
                listView.removeLoadingFooterView();
                mSpinner.setEnabled(true);
            };
        };
    }

    private Response.ErrorListener createSearchRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, error.getMessage(), error);
                getActivity().setProgressBarIndeterminateVisibility(false);
                isLoading = false;
                listView.removeLoadingFooterView();
                mSpinner.setSelection(mPrevChannelPos);
                mCurrChannelPos = mPrevChannelPos;
                mSpinner.setEnabled(true);
            }
        };
    }

    /**
     * This method retrieves the Library of videos from the retrieved items and
     * passes them to our ListView
     * @param items
     */
    private void populateListWithVideos(ArrayList<YouTubeSearchResultItemsModel> items) {
        // Create a list to store are videos in
        final List<YouTubeVideo> videos = new ArrayList<YouTubeVideo>();
        for (int i = 0; i < items.size(); i++) {
            final YouTubeSearchResultItemsModel itemsModel = items.get(i);

            final YouTubeSearchItemSnippetModel itemSnippet = itemsModel.getSnippet();
            final String title = itemSnippet.getTitle();
            final String videoId = itemsModel.getId().getVideoId();
            final String thumbUrl = itemSnippet.getThumbnails().getDefaultThumbnail().getUrl();
            Date d = null;
            try {
                d = RETRIEVED_DATE_FORMAT.parse(itemSnippet.getPublishedAt());
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            final String formattedTime = (d != null) ? OUTPUT_DATE_FORMAT.format(d) : "";
            // Create the video object and add it to our list
            videos.add(new YouTubeVideo(
                    itemSnippet.getTitle(), YouTubeApiUriRequestBuilder.WATCH_BASE_URL + videoId,
                    thumbUrl, formattedTime, itemSnippet.getDescription()));
        }
        mLibrary.addVideos(videos);

        getActivity().setProgressBarIndeterminateVisibility(false);
        mYouTubeVideoAdapter.updateAdapter(mLibrary.getVideos());
    }

    @Override
    public void onStop() {
        super.onStop();
        // Only cancel requests from this fragment
        RequestQueueSingleton.getInstance(getActivity().getApplicationContext())
                .cancelPendingRequests(TAG);
    }

    @Override
    public void onDestroy() {
        listView.destroyDrawingCache();
        listView = null;
        mYouTubeVideoAdapter.notifyDataSetInvalidated();
        mYouTubeVideoAdapter = null;
        mSpinner = null;
        mLibrary = null;

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
                getUserYouTubeFeed(getActivity());
            }
        }
    }

}
