package com.almaghrib.mobile;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribJournalModelContainer;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class LatestJournalCardAdapter extends RecyclerView.Adapter<LatestJournalCardAdapter.LatestJournalCardViewHolder> {

    private final Activity mActivity;
    private final ImageLoader mImageLoader;
    private List<AlMaghribJournalModelContainer> mDataset;

    // Provide a reference to the views for each data item
    public static class LatestJournalCardViewHolder extends RecyclerView.ViewHolder {
        protected FeedImageView mBannerImageView;
        protected TextView mJournalTitleTextView;

        public LatestJournalCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mJournalTitleTextView = (TextView) v.findViewById(R.id.titleTextView);
        }
    }

    public LatestJournalCardAdapter(Activity activity, List<AlMaghribJournalModelContainer> dataset) {
        mActivity = activity;
        mDataset = dataset;
        mImageLoader = RequestQueueSingleton.getInstance(activity.getApplicationContext()).getImageLoader();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LatestJournalCardAdapter.LatestJournalCardViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        // create a new view
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_journal_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new LatestJournalCardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final LatestJournalCardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final AlMaghribJournalModelContainer bannerModel = mDataset.get(position);

        holder.mBannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
        holder.mBannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);

        holder.mJournalTitleTextView.setText(bannerModel.getTitle());

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

    public void updateItems(List<AlMaghribJournalModelContainer> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

}
