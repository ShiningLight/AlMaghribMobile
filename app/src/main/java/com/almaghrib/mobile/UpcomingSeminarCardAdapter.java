package com.almaghrib.mobile;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarBannerModel;
import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarsLocationListModelContainer;
import com.almaghrib.mobile.util.view.AVLoadingIndicatorButton;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.almaghrib.mobile.util.view.HeaderRecyclerViewAdapter;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class UpcomingSeminarCardAdapter extends HeaderRecyclerViewAdapter<
        RecyclerView.ViewHolder, AlMaghribUpcomingSeminarsLocationListModelContainer,
        AlMaghribUpcomingSeminarsLocationListModelContainer, AlMaghribUpcomingSeminarsLocationListModelContainer> {

    private final Activity mActivity;
    private final ImageLoader mImageLoader;
    private final onRetrieveDataSelectedListener mDataSelectedListener;
    private List<AlMaghribUpcomingSeminarBannerModel> mDataset;

    /**
     * Interface to react to changes in value of item selection triggered by button click
     */
    public interface onRetrieveDataSelectedListener {
        void onDataSelected(String selectedItem, AVLoadingIndicatorButton button, boolean makeRequest);
    }

    // Provide a reference to the views for each data item
    private static class HeaderSeminarCardViewHolder extends RecyclerView.ViewHolder {
        private FeedImageView mBannerImageView;
        private TextView mSeminarNameTextView;
        private Spinner mSpinner;
        private AVLoadingIndicatorButton mGoButton;

        public HeaderSeminarCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mSeminarNameTextView = (TextView) v.findViewById(R.id.titleTextView);
            mSpinner = (Spinner) v.findViewById(R.id.spinner);
            mGoButton = (AVLoadingIndicatorButton) v.findViewById(R.id.goButton);
        }
    }

    // Provide a reference to the views for each data item
    private static class SeminarCardViewHolder extends RecyclerView.ViewHolder {
        private FeedImageView mBannerImageView;
        private TextView mSeminarNameTextView;
        private TextView mDateView;
        private TextView mVenueTextView;

        public SeminarCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mSeminarNameTextView = (TextView) v.findViewById(R.id.titleTextView);
            mDateView = (TextView) v.findViewById(R.id.dateTextView);
            mVenueTextView = (TextView) v.findViewById(R.id.venueTextView);
        }
    }

    /**
     * Constructor for adapter to create list of cards for upcoming seminars page
     * @param activity
     * @param dataset
     * @param dataSelectedListener
     */
    public UpcomingSeminarCardAdapter(Activity activity, List<AlMaghribUpcomingSeminarBannerModel> dataset,
                                      onRetrieveDataSelectedListener dataSelectedListener) {
        mActivity = activity;
        mDataset = dataset;
        mDataSelectedListener = dataSelectedListener;
        mImageLoader = RequestQueueSingleton.getInstance(activity.getApplicationContext()).getImageLoader();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_your_item_card, parent, false);
        return new HeaderSeminarCardViewHolder(v);
    }

    @Override
    protected SeminarCardViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_seminar, parent, false);
        return new SeminarCardViewHolder(v);
    }

    @Override
    protected SeminarCardViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HeaderSeminarCardViewHolder headerItemHolder = (HeaderSeminarCardViewHolder) holder;

        final AlMaghribUpcomingSeminarsLocationListModelContainer model = getHeader();

        headerItemHolder.mBannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
        //bannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);

        headerItemHolder.mSeminarNameTextView.setText(model.getTitle());

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.select_item_spinner_text_view, model.getCities());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        headerItemHolder.mSpinner.setAdapter(adapter);

        headerItemHolder.mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headerItemHolder.mGoButton.getText().equals("")) {
                    headerItemHolder.mGoButton.setShowIndicator(false);
                    headerItemHolder.mGoButton.setText(R.string.startup_screen_go_text);
                    mDataSelectedListener.onDataSelected(
                            headerItemHolder.mSpinner.getSelectedItem().toString(),
                            headerItemHolder.mGoButton, false);
                } else {
                    headerItemHolder.mGoButton.setText("");
                    headerItemHolder.mGoButton.setShowIndicator(true);
                    mDataSelectedListener.onDataSelected(
                            headerItemHolder.mSpinner.getSelectedItem().toString(),
                            headerItemHolder.mGoButton, true);
                }
            }
        });
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final AlMaghribUpcomingSeminarBannerModel bannerModel = mDataset.get(position);

        final SeminarCardViewHolder mainItemHolder = (SeminarCardViewHolder) holder;

        mainItemHolder.mBannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
        mainItemHolder.mBannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);
        final String transitionName = mActivity.getString(R.string.seminar_banner_name) + position;
        ViewCompat.setTransitionName(mainItemHolder.mBannerImageView, transitionName);

        mainItemHolder.mSeminarNameTextView.setText(bannerModel.getSeminarName());
        mainItemHolder.mDateView.setText(bannerModel.getInstructorName());
        mainItemHolder.mVenueTextView.setText(bannerModel.getUpcomingLocation() + " - " + bannerModel.getDate());

        // set on click listener on view so we still get ripple effect
        mainItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNextFragment(mainItemHolder.mBannerImageView, false, bannerModel);
            }
        });
    }

    public void updateItems(List<AlMaghribUpcomingSeminarBannerModel> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    private void addNextFragment(ImageView imageView, boolean overlap, AlMaghribUpcomingSeminarBannerModel bannerModel) {
        final String transitionName = ViewCompat.getTransitionName(imageView);
        SeminarInfoFragment sharedElementFragment2 = SeminarInfoFragment.init(transitionName, bannerModel);

        sharedElementFragment2.setSharedElementEnterTransition(new DetailsTransition());
        sharedElementFragment2.setEnterTransition(new Fade());
        sharedElementFragment2.setSharedElementReturnTransition(new DetailsTransition());


        final HomePageActivity homePageActivity = (HomePageActivity) mActivity;
        homePageActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, sharedElementFragment2)
                        //.addToBackStack(null)
                .addSharedElement(imageView, transitionName)
                .commit();
    }

    private class DetailsTransition extends TransitionSet {
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }
}
