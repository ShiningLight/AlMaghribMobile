package com.almaghrib.mobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.almaghrib.mobile.almaghribApi.data.AlMaghribApiUriRequestBuilder;
import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribHomeSeminarModelContainer;
import com.almaghrib.mobile.util.GsonRequest;
import com.almaghrib.mobile.util.view.CircleNetworkImageView;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;


public class AlMaghribHomeFragment extends Fragment {
    private static final String TAG = "AlMaghribHomeFragment";

    private ImageLoader mImageLoader;
    private ProgressBar mProgressBar;

    public AlMaghribHomeFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getActivity().getApplicationContext();
        mImageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();

        // make request for home page seminar item
        final String url = new AlMaghribApiUriRequestBuilder().buildHomeSeminarRequest();

        final GsonRequest<AlMaghribHomeSeminarModelContainer> request =
                new GsonRequest<AlMaghribHomeSeminarModelContainer>(
                        Request.Method.GET, url,
                        AlMaghribHomeSeminarModelContainer.class,
                        createHomeSeminarRequestSuccessListener(),
                        createHomeSeminarRequestErrorListener());
        request.setTag(TAG);
        // TODO uncomment RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.almaghrib_home_page, container, false);

        final View seminarCardsView = layoutView.findViewById(R.id.seminarCardsLayout);
        if (seminarCardsView != null) {
            //TODO uncomment seminarCardsView.setVisibility(View.GONE);
        }
        mProgressBar = (ProgressBar) layoutView.findViewById(R.id.progressBar);
        //TODO remove this code
        mProgressBar.setVisibility(View.GONE);
        final FeedImageView seminarBannerView =
                (FeedImageView) seminarCardsView.findViewById(R.id.seminarBannerImageView);
        seminarBannerView.setDefaultImageResId(R.drawable.waleed);
        final CircleNetworkImageView instructorImage =
                (CircleNetworkImageView) seminarCardsView.findViewById(R.id.profile_image);
        instructorImage.setDefaultImageResId(R.drawable.waleed);

        return layoutView;
    }

    private Response.Listener<AlMaghribHomeSeminarModelContainer> createHomeSeminarRequestSuccessListener() {
        return new Response.Listener<AlMaghribHomeSeminarModelContainer>() {
            @Override
            public void onResponse(AlMaghribHomeSeminarModelContainer response) {
                try {
                    Log.d(TAG, response.toString());
                    updateHomeSeminarUI(response);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                mProgressBar.setVisibility(View.GONE);
            };
        };
    }

    private Response.ErrorListener createHomeSeminarRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                mProgressBar.setVisibility(View.GONE);
            }
        };
    }

    private void updateHomeSeminarUI(AlMaghribHomeSeminarModelContainer response) {
        final View layoutView = getView();
        if (layoutView != null) {
            final View seminarCardsView = layoutView.findViewById(R.id.seminarCardsLayout);
            if (seminarCardsView != null) {
                seminarCardsView.setVisibility(View.VISIBLE);

                // get seminar banner info from response
                final View seminarBannerCardView = seminarCardsView.findViewById(R.id.seminar_banner_card_view);
                if (seminarBannerCardView != null) {
                    updateSeminarBannerUI(seminarBannerCardView);
                }

                // get instr. info from response
                final View seminarInstructorCardView = seminarCardsView.findViewById(R.id.seminar_instructor_card_view);
                if (seminarInstructorCardView != null) {
                    updateSeminarInstructorBannerUI(seminarInstructorCardView);
                }

                // update testimonial ui

            }
        }
    }

    private void updateSeminarBannerUI(View seminarBannerCardView) {
        final FeedImageView seminarBannerView =
                (FeedImageView) seminarBannerCardView.findViewById(R.id.seminarBannerImageView);
        seminarBannerView.setDefaultImageResId(R.drawable.waleed);
        seminarBannerView.setImageUrl("", mImageLoader);


    }

    private void updateSeminarInstructorBannerUI(View seminarInstructorCardView) {
        final CircleNetworkImageView instructorImage =
                (CircleNetworkImageView) seminarInstructorCardView.findViewById(R.id.profile_image);
        instructorImage.setDefaultImageResId(R.drawable.waleed);
    }

    @Override
    public void onDestroy() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        super.onDestroy();
    }
}
