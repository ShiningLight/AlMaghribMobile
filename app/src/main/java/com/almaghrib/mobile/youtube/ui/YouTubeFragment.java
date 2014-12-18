package com.almaghrib.mobile.youtube.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.youtube.tasks.GetYouTubeUserVideosTask;
import com.almaghrib.mobile.youtube.tasks.YouTubeVideoLibrary;

public class YouTubeFragment extends Fragment {
    int fragVal;
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
        View layoutView = inflater.inflate(R.layout.youtube_page, container,
                false);
        listView = (YouTubeVideoListView) layoutView
                .findViewById(R.id.videosListView);
        Button b = (Button) layoutView.findViewById(R.id.getFeedButton);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserYouTubeFeed(v);
            }
        });
        return layoutView;
    }
    

    // This is the XML onClick listener to retreive a users video feed
    public void getUserYouTubeFeed(View v){
        // This is the handler that receives the response when the YouTube task has finished
        Handler responseHandler = new Handler() {
            public void handleMessage(Message msg) {
                populateListWithVideos(msg);
            };
        };
        // We start a new task that does its work on its own thread
        // We pass in a handler that will be called when the task has finished
        // We also pass in the name of the user we are searching YouTube for
        new Thread(new GetYouTubeUserVideosTask(
                responseHandler, "AlMaghribTrailers")).start();
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
    }
    
}
