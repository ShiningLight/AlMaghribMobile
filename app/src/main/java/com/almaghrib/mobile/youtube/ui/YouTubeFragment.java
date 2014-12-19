package com.almaghrib.mobile.youtube.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchModelContainer;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchResultItemsModel;
import com.almaghrib.mobile.youtube.tasks.GetYouTubeUserVideosTask;
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
import java.util.HashMap;
import java.util.List;

public class YouTubeFragment extends Fragment {
    protected final static String TAG = YouTubeFragment.class.getSimpleName();

    private static final SimpleDateFormat RETRIEVED_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT =
            new SimpleDateFormat("dd-MM-yyyy");

    private int fragVal;
    private YouTubeVideoListView listView;

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
        final Button b = (Button) layoutView.findViewById(R.id.getFeedButton);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: get id from file matching text on spinner (to add)
                getUserYouTubeFeed(v);
            }
        });
        return layoutView;
    }
    

    // This is the XML onClick listener to retreive a users video feed
    public void getUserYouTubeFeed(View v){
        final String channelId = "UC1B7kOnMRdPuP0rxW6mS7_A"; // TODO: remove hardcoded string for AlMaghribTrailers

        String url = YouTubeConstants.BASE_REQUEST_URL + YouTubeConstants.SEARCH_REQUEST +
                "?key=AIzaSyCq5TVzGp1J6_nPCLwaiHfs6C4gSSbHzuM&channelId=" + channelId + "&part=snippet,id&order=date&maxResults=20";

/*        // Create parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", "AIzaSyCq5TVzGp1J6_nPCLwaiHfs6C4gSSbHzuM");
        params.put("channelId", channelId);
        params.put("part", "snippet,id");
        params.put("order", "date");
        params.put("maxResults", "20");*/

        final GsonRequest<YouTubeSearchModelContainer> myReq =
                new GsonRequest<YouTubeSearchModelContainer>(
                        Request.Method.GET,
                        url,
                        YouTubeSearchModelContainer.class,
                        //params,
                        createMyReqSuccessListener(channelId),
                        createMyReqErrorListener());

        RequestQueueSingleton.getInstance(v.getContext().getApplicationContext())
                .addToRequestQueue(myReq);
    }

    private Response.Listener<YouTubeSearchModelContainer> createMyReqSuccessListener(final String channelId) {
        return new Response.Listener<YouTubeSearchModelContainer>() {
            @Override
            public void onResponse(YouTubeSearchModelContainer response) {
                try {
                    Log.d(TAG, response.toString());
                    populateListWithVideos(response.getItems(), channelId);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            };
        };
    }

    private void populateListWithVideos(ArrayList<YouTubeSearchResultItemsModel> items, String channelId) {
        // Create a list to store are videos in
        final List<YouTubeVideo> videos = new ArrayList<YouTubeVideo>();
        for (int i = 0; i < items.size(); i++) {
            final YouTubeSearchResultItemsModel itemsModel = items.get(i);
            final String title = itemsModel.getSnippet().getTitle();
            final String videoId = itemsModel.getId().getVideoId();
            final String thumbUrl = itemsModel.getSnippet().getThumbnails()
                    .getDefaultThumbnail().getUrl();
            Date d = null;
            try {
                d = RETRIEVED_DATE_FORMAT.parse(itemsModel.getSnippet().getPublishedAt());
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            final String formattedTime = (d != null) ? OUTPUT_DATE_FORMAT.format(d) : "";
            // Create the video object and add it to our list
            videos.add(new YouTubeVideo(
                    title, YouTubeConstants.WATCH_BASE_URL + videoId, thumbUrl, formattedTime));
        }
        // Create a library to hold our videos
        final YouTubeVideoLibrary lib = new YouTubeVideoLibrary(channelId, videos);
        // Because we have created a custom ListView we don't have to worry about setting the adapter
        // in the activity we can just call our custom method with the list of items we want to display
        listView.setVideos(lib.getVideos());
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, error.getMessage(), error);
            }
        };
    }
    
    /**
     * This method retrieves the Library of videos from the task and passes them to our ListView
     * @param msg
     */
    private void populateListWithVideos(Message msg) {
        // Retreive the videos are task found from the data bundle sent back
        YouTubeVideoLibrary lib = (YouTubeVideoLibrary) msg.getData().get(
                GetYouTubeUserVideosTask.LIBRARY);
        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
        // we can just call our custom method with the list of items we want to display
        listView.setVideos(lib.getVideos());
    }
     
    @Override
    public void onStop() {
        // Make sure we null our handler when the activity has stopped
        // because who cares if we get a callback once the activity has stopped? not me!
        //responseHandler = null;
        super.onStop();
        // Only cancel requests from this fragment
        RequestQueueSingleton.getInstance(getActivity().getApplicationContext())
                .cancelPendingRequests(TAG);
    }
    
}
