package com.almaghrib.mobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarBannerModel;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class SeminarCardAdapter extends RecyclerView.Adapter<SeminarCardAdapter.SeminarCardViewHolder> {

    private final ImageLoader mImageLoader;
    private List<AlMaghribUpcomingSeminarBannerModel> mDataset;

    // Provide a reference to the views for each data item
    public static class SeminarCardViewHolder extends RecyclerView.ViewHolder {
        protected FeedImageView mBannerImageView;
        protected TextView mUpcomingTextView;
        protected TextView mSeminarNameTextView;
        protected TextView mDateView;
        protected TextView mVenueTextView;

        public SeminarCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mUpcomingTextView = (TextView) v.findViewById(R.id.upcomingTextView);
            mSeminarNameTextView = (TextView) v.findViewById(R.id.titleTextView);
            mDateView = (TextView) v.findViewById(R.id.dateTextView);
            mVenueTextView = (TextView) v.findViewById(R.id.venueTextView);
        }
    }

    public SeminarCardAdapter(Context context, List<AlMaghribUpcomingSeminarBannerModel> dataset) {
        mDataset = dataset;
        mImageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SeminarCardAdapter.SeminarCardViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        // create a new view
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_seminar, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new SeminarCardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SeminarCardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final AlMaghribUpcomingSeminarBannerModel bannerModel = mDataset.get(position);

        holder.mBannerImageView.setDefaultImageResId(R.drawable.waleed);
        holder.mBannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);

        holder.mUpcomingTextView.setText(bannerModel.getUpcomingLocation());
        holder.mSeminarNameTextView.setText(bannerModel.getSeminarName());
        holder.mDateView.setText(bannerModel.getDate());
        holder.mVenueTextView.setText(bannerModel.getVenue());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateItems(List<AlMaghribUpcomingSeminarBannerModel> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }
    
}
