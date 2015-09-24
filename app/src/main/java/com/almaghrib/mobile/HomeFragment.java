package com.almaghrib.mobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.almaghrib.mobile.youtube.data.YouTubeApiUriRequestBuilder;
import com.almaghrib.mobile.youtube.data.YouTubeVideo;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchItemSnippetModel;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchModelContainer;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchResultItemsModel;
import com.almaghrib.mobile.youtube.ui.YouTubeFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final String SINGLE_ITEM = "1";

    private ImageLoader mImageLoader;

    private YouTubeVideo mMainVideo;

    public HomeFragment() {
        super();
    }

    public static String getFragmentName() {
        return "Home"; //TODO: use strings
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getActivity().getApplicationContext();
        mImageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();

        final String url = new YouTubeApiUriRequestBuilder().buildSearchRequest(
                (context.getResources().getTextArray(R.array.channel_ids)[0]).toString(),
                null, SINGLE_ITEM);

        final GsonRequest<YouTubeSearchModelContainer> request =
                new GsonRequest<YouTubeSearchModelContainer>(
                        Request.Method.GET, url,
                        YouTubeSearchModelContainer.class,
                        createSearchRequestSuccessListener(),
                        createSearchRequestErrorListener());
        request.setTag(TAG);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.home_page, container, false);

        if (mMainVideo != null) {
            setupVideoView(mMainVideo);
        }

        final ListView lv = (ListView) layoutView.findViewById(R.id.newsListView);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        Map<String, String> datum = new HashMap<String, String>();
        datum.put("title", "AlMaghrib launches in Malaysia");
        datum.put("desc", "With over 1500 people in attendance, AlMaghrib Institute has launched in Malaysia...");
        data.add(datum);
        datum = new HashMap<String, String>();
        datum.put("title", "Sh Waleed comes to Toronto");
        datum.put("desc", "Heard that Toronto? Sh WaleedBasyouni is coming to town for another IlmNight...");
        data.add(datum);
        datum = new HashMap<String, String>();
        datum.put("title", "Sh Waleed comes to 22222222222222222");
        datum.put("desc", "Heard that Toronto? Sh WaleedBasyouni is coming to town222222222222...");
        data.add(datum);
        datum = new HashMap<String, String>();
        datum.put("title", "Sh Waleed comes to 333333333");
        datum.put("desc", "Heard that Toronto? Sh WaleedBasyouni 3333333333333t...");
        data.add(datum);

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), data,
                R.layout.news_item,
                new String[] {"title", "desc"},
                new int[] {R.id.text1, R.id.text2});

        lv.setAdapter(adapter);

        final View headerView = inflater.inflate(R.layout.home_screen_header, null, false);
        lv.addHeaderView(headerView);

        final Button moreVideosButton = (Button) headerView.findViewById(R.id.moreButton);
        moreVideosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HomePageActivity homePageActivity = (HomePageActivity) getActivity();
                HomePageActivity.startFragment(homePageActivity, new SocialUpdatesFragment());
                homePageActivity.setDrawerItem(R.id.social, getString(R.string.social));
            }
        });

        return layoutView;
    }

    private Response.Listener<YouTubeSearchModelContainer> createSearchRequestSuccessListener() {
        return new Response.Listener<YouTubeSearchModelContainer>() {
            @Override
            public void onResponse(YouTubeSearchModelContainer response) {
                try {
                    Log.d(TAG, response.toString());
                    setupMainVideo(response.getItems());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                if (getActivity() != null) {
                    getActivity().setProgressBarIndeterminateVisibility(false);
                }
            };
        };
    }

    private Response.ErrorListener createSearchRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                if (getActivity() != null) {
                    getActivity().setProgressBarIndeterminateVisibility(false);
                }
            }
        };
    }

    private void setupMainVideo(ArrayList<YouTubeSearchResultItemsModel> items) {
        YouTubeVideo mainVideo;

        if (!items.isEmpty()) {
            final YouTubeSearchResultItemsModel itemsModel = items.get(0);

            final YouTubeSearchItemSnippetModel itemSnippet = itemsModel.getSnippet();
            final String title = itemSnippet.getTitle();
            final String videoId = itemsModel.getId().getVideoId();
            final String thumbUrl = itemSnippet.getThumbnails().getHigh().getUrl();
            Date d = null;
            try {
                d = YouTubeFragment.RETRIEVED_DATE_FORMAT.parse(itemSnippet.getPublishedAt());
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            final String formattedTime = (d != null) ? YouTubeFragment.OUTPUT_DATE_FORMAT.format(d) : "";
            // Create the video object and add it to our list
            mainVideo = new YouTubeVideo(title, YouTubeApiUriRequestBuilder.WATCH_BASE_URL + videoId,
                    thumbUrl, formattedTime, null);
            if (!mainVideo.equals(mMainVideo)) {
                mMainVideo = mainVideo;
                setupVideoView(mainVideo);
            }

        }
    }

    private void setupVideoView(YouTubeVideo mainVideo) {
        final View layoutView = getView();
        if (layoutView != null) {
            final FeedImageView imageView = (FeedImageView) layoutView.findViewById(R.id.mainVideoImageView);
            if (imageView != null) {
                imageView.setImageUrl(mainVideo.getThumbUrl(), mImageLoader);
            }
            final TextView titleView = (TextView) layoutView.findViewById(R.id.videoTitle);
            titleView.setText(mainVideo.getTitle());
            final TextView dateView = (TextView) layoutView.findViewById(R.id.videoDate);
            dateView.setText(mainVideo.getPublishedDate());
        }
    }

}
