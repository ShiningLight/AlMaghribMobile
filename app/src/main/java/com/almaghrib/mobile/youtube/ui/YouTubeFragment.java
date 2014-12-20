package com.almaghrib.mobile.youtube.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchItemSnippetModel;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchModelContainer;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchResultItemsModel;
import com.almaghrib.mobile.youtube.tasks.YouTubeConstants;
import com.almaghrib.mobile.youtube.tasks.YouTubeVideo;
import com.almaghrib.mobile.youtube.tasks.YouTubeVideoLibrary;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YouTubeFragment extends Fragment implements AbsListView.OnScrollListener {
    protected final static String TAG = YouTubeFragment.class.getSimpleName();

    private static final SimpleDateFormat RETRIEVED_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT =
            new SimpleDateFormat("dd-MM-yyyy");

    private int fragVal;

    private YouTubeVideoListView listView;
    private YouTubeVideoAdapter mYouTubeVideoAdapter;

    private String mChannelId;
    private YouTubeVideoLibrary mLibrary;

    private boolean mIsUserScrolling = false;
    private String mNextPageToken;
    private boolean isLoading;

    public static YouTubeFragment init(int val) {
        YouTubeFragment truitonFrag = new YouTubeFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
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

        listView = (YouTubeVideoListView) layoutView.findViewById(R.id.videosListView);
        listView.setOnScrollListener(this);
        mYouTubeVideoAdapter = new YouTubeVideoAdapter(getActivity(), new ArrayList<YouTubeVideo>());
        listView.setAdapter(mYouTubeVideoAdapter);

        final Button b = (Button) layoutView.findViewById(R.id.getFeedButton);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: get id from file matching text on spinner (to add)
                mChannelId = "UC1B7kOnMRdPuP0rxW6mS7_A"; // TODO: remove hardcoded string for AlMaghribTrailers
                // Create a library to hold our videos
                mLibrary = new YouTubeVideoLibrary(mChannelId, new ArrayList<YouTubeVideo>());
                mNextPageToken = null;
                getUserYouTubeFeed(v.getContext());
            }
        });
        return layoutView;
    }

    /**
     * onClick listener to retrieve a users video feed
     * @param context
     */
    public void getUserYouTubeFeed(Context context){
        String url = YouTubeConstants.BASE_REQUEST_URL + YouTubeConstants.SEARCH_REQUEST +
                "?key=AIzaSyCq5TVzGp1J6_nPCLwaiHfs6C4gSSbHzuM&channelId=" + mChannelId +
                "&part=snippet,id&order=date&maxResults=15&type=video";
        if (mNextPageToken != null) {
            url += "&pageToken=" + mNextPageToken;
        }
/*        // Create parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", "AIzaSyCq5TVzGp1J6_nPCLwaiHfs6C4gSSbHzuM");
        params.put("channelId", channelId);
        params.put("part", "snippet,id");
        params.put("order", "date");
        params.put("maxResults", "20");*/

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
                }
                isLoading = false;
                listView.removeLoadingFooterView();
            };
        };
    }

    private Response.ErrorListener createSearchRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, error.getMessage(), error);
                isLoading = false;
                listView.removeLoadingFooterView();
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
                    title, YouTubeConstants.WATCH_BASE_URL + videoId, thumbUrl, formattedTime));
        }
        mLibrary.addVideos(videos);

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
