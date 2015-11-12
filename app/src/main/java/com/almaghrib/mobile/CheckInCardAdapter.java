package com.almaghrib.mobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribCheckInModelContainer;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class CheckInCardAdapter extends RecyclerView.Adapter<CheckInCardAdapter.CheckInCardViewHolder> {

    private final ImageLoader mImageLoader;
    private List<AlMaghribCheckInModelContainer> mDataset;

    // Provide a reference to the views for each data item
    protected static class CheckInCardViewHolder extends RecyclerView.ViewHolder {
        protected FeedImageView mQRCodeImageView;
        protected TextView mSeminarNameTextView;
        protected TextView mDateView;
        protected TextView mVenueTextView;

        public CheckInCardViewHolder(View v) {
            super(v);
            mQRCodeImageView = (FeedImageView) v.findViewById(R.id.qrCodeImageView);
            mSeminarNameTextView = (TextView) v.findViewById(R.id.seminarTextView);
            mDateView = (TextView) v.findViewById(R.id.dateTextView);
            mVenueTextView = (TextView) v.findViewById(R.id.venueTextView);
        }
    }

    public CheckInCardAdapter(Context context, List<AlMaghribCheckInModelContainer> dataset) {
        mDataset = dataset;
        mImageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CheckInCardAdapter.CheckInCardViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        // create a new view
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_in_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new CheckInCardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CheckInCardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final AlMaghribCheckInModelContainer bannerModel = mDataset.get(position);

        holder.mQRCodeImageView.setDefaultImageResId(R.drawable.qr);
        holder.mQRCodeImageView.setImageUrl(bannerModel.getQrCodeUrl(), mImageLoader);

        holder.mSeminarNameTextView.setText(bannerModel.getSeminarName());
        holder.mDateView.setText(bannerModel.getDate());
        holder.mVenueTextView.setText(bannerModel.getVenue());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateItems(List<AlMaghribCheckInModelContainer> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

}
