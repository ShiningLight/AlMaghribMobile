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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribJournalModelContainer;
import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribNewsModelContainer;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class NewsCardAdapter extends RecyclerView.Adapter<NewsCardAdapter.NewsCardViewHolder> {

    private static final int TYPE_VIDEO   = 0;
    private static final int TYPE_ARTICLE = 1;

    private final Activity mActivity;
    private final ImageLoader mImageLoader;
    private List<AlMaghribNewsModelContainer> mDataset;

    // Provide a reference to the views for each data item
    protected static class NewsCardViewHolder extends RecyclerView.ViewHolder {
        protected FeedImageView mBannerImageView;
        protected TextView mJournalTitleTextView;
        protected Button mReadMoreButton;

        public NewsCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mJournalTitleTextView = (TextView) v.findViewById(R.id.titleTextView);
            mReadMoreButton = (Button) v.findViewById(R.id.readMoreButton);
        }
    }
    // Provide a reference to the views for each data item
    protected static class VideoNewsCardViewHolder extends NewsCardViewHolder {
        protected ImageButton mPlayButton;

        public VideoNewsCardViewHolder(View v) {
            super(v);
            mPlayButton = (ImageButton) v.findViewById(R.id.playImageButton);
        }
    }

    /**
     * Constructor for News card adapter
     * @param activity
     * @param dataset
     */
    public NewsCardAdapter(Activity activity, List<AlMaghribNewsModelContainer> dataset) {
        mActivity = activity;
        mDataset = dataset;
        mImageLoader = RequestQueueSingleton.getInstance(activity.getApplicationContext()).getImageLoader();
    }

    @Override
    public int getItemViewType (int position) {
        int viewType;
        if (mDataset.get(position).getVideoUrl() != null) {
            viewType = TYPE_VIDEO;
        } else {
            viewType = TYPE_ARTICLE;
        }
        return viewType;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsCardAdapter.NewsCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VIDEO:
                // create a new view
                final View vVideo = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_video_item_card, parent, false);
                // set the view's size, margins, paddings and layout parameters
                return new VideoNewsCardViewHolder(vVideo);
            case TYPE_ARTICLE:
            default:
                // create a new view
                final View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_regular_item_card, parent, false);
                // set the view's size, margins, paddings and layout parameters
                return new NewsCardViewHolder(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final NewsCardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final AlMaghribNewsModelContainer bannerModel = mDataset.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_VIDEO:
                final VideoNewsCardViewHolder videoNewsHolder = (VideoNewsCardViewHolder) holder;
                // TODO: something for play button click

                break;
            case TYPE_ARTICLE:
                break;
        }

        holder.mBannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
        holder.mBannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);
        holder.mJournalTitleTextView.setText(bannerModel.getTitle());
        final String transitionName = mActivity.getString(R.string.seminar_banner_name) + position;
        ViewCompat.setTransitionName(holder.mBannerImageView, transitionName);

        final View.OnClickListener openArticleListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNextFragment(holder.mBannerImageView, bannerModel);
            }
        };
        // set on click listener on view so we still get ripple effect
        holder.itemView.setOnClickListener(openArticleListener);
        holder.mReadMoreButton.setOnClickListener(openArticleListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateItems(List<AlMaghribNewsModelContainer> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    private void addNextFragment(ImageView imageView, AlMaghribNewsModelContainer model) {
        final String transitionName = ViewCompat.getTransitionName(imageView);
        NewsArticleFragment sharedElementFragment2 = NewsArticleFragment.init(transitionName, model);

        sharedElementFragment2.setSharedElementEnterTransition(new DetailsTransition());
        sharedElementFragment2.setEnterTransition(new Fade());
        //setExitTransition(new Fade());
        sharedElementFragment2.setSharedElementReturnTransition(new DetailsTransition());

        final HomePageActivity homePageActivity = (HomePageActivity) mActivity;
        homePageActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, sharedElementFragment2)
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
