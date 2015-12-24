package com.almaghrib.mobile;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public NewsCardViewHolder(View v) {
            super(v);
        }
    }
    // Provide a reference to the views for each data item
    protected static class VideoNewsCardViewHolder extends NewsCardViewHolder {
        protected FeedImageView mBannerImageView;
        protected TextView mJournalTitleTextView;

        public VideoNewsCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mJournalTitleTextView = (TextView) v.findViewById(R.id.titleTextView);
        }
    }
    // Provide a reference to the views for each data item
    protected static class ArticleNewsCardViewHolder extends NewsCardViewHolder {
        protected FeedImageView mBannerImageView;
        protected TextView mJournalTitleTextView;

        public ArticleNewsCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mJournalTitleTextView = (TextView) v.findViewById(R.id.titleTextView);
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
                        .inflate(R.layout.latest_journal_card, parent, false);
                // set the view's size, margins, paddings and layout parameters
                return new ArticleNewsCardViewHolder(v);
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
                videoNewsHolder.mBannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
                videoNewsHolder.mBannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);
                videoNewsHolder.mJournalTitleTextView.setText(bannerModel.getTitle());
                break;
            case TYPE_ARTICLE:
                final ArticleNewsCardViewHolder articleNewsHolder = (ArticleNewsCardViewHolder) holder;
                articleNewsHolder.mBannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
                articleNewsHolder.mBannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);
                articleNewsHolder.mJournalTitleTextView.setText(bannerModel.getTitle());
                break;
        }

        // set on click listener on view so we still get ripple effect
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNextFragment(holder.mBannerImageView, false);
            }
        });
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

}
