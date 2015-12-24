package com.almaghrib.mobile;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribUpcomingSeminarBannerModel;
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class SeminarCardAdapter extends RecyclerView.Adapter<SeminarCardAdapter.SeminarCardViewHolder> {

    private final Activity mActivity;
    private final ImageLoader mImageLoader;
    private List<AlMaghribUpcomingSeminarBannerModel> mDataset;

    // Provide a reference to the views for each data item
    public static class SeminarCardViewHolder extends RecyclerView.ViewHolder {
        protected FeedImageView mBannerImageView;
        protected TextView mSeminarNameTextView;
        protected TextView mDateView;
        protected TextView mVenueTextView;

        public SeminarCardViewHolder(View v) {
            super(v);
            mBannerImageView = (FeedImageView) v.findViewById(R.id.seminarBannerImageView);
            mSeminarNameTextView = (TextView) v.findViewById(R.id.titleTextView);
            mDateView = (TextView) v.findViewById(R.id.dateTextView);
            mVenueTextView = (TextView) v.findViewById(R.id.venueTextView);
        }
    }

    public SeminarCardAdapter(Activity activity, List<AlMaghribUpcomingSeminarBannerModel> dataset) {
        mActivity = activity;
        mDataset = dataset;
        mImageLoader = RequestQueueSingleton.getInstance(activity.getApplicationContext()).getImageLoader();
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
    public void onBindViewHolder(final SeminarCardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final AlMaghribUpcomingSeminarBannerModel bannerModel = mDataset.get(position);

        holder.mBannerImageView.setDefaultImageResId(R.drawable.love_notes_card);
        holder.mBannerImageView.setImageUrl(bannerModel.getBannerUrl(), mImageLoader);
        final String transitionName = mActivity.getString(R.string.seminar_banner_name) + position;
        ViewCompat.setTransitionName(holder.mBannerImageView, transitionName);

        holder.mSeminarNameTextView.setText(bannerModel.getSeminarName());
        holder.mDateView.setText(bannerModel.getInstructorName());
        holder.mVenueTextView.setText(bannerModel.getUpcomingLocation() + " - " + bannerModel.getDate());

        // set on click listener on view so we still get ripple effect
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNextFragment(holder.mBannerImageView, false);
            }
        });
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

    private void addNextFragment(ImageView imageView, boolean overlap) {
        final String transitionName = ViewCompat.getTransitionName(imageView);
        SeminarInfoFragment sharedElementFragment2 = SeminarInfoFragment.init(transitionName);

        // Defines enter transition for all fragment views
//        Slide slideTransition = new Slide(Gravity.BOTTOM);
//        slideTransition.setDuration(800);//mContext.getResources().getInteger(R.integer.anim_duration_medium));
//        sharedElementFragment2.setEnterTransition(slideTransition);
//
////        ChangeBounds changeBoundsTransition = new ChangeBounds();
////        changeBoundsTransition.setDuration(1000);//getResources().getInteger(R.integer.anim_duration_medium));
//        // Defines enter transition only for shared element
//        Transition changeBoundsTransition = TransitionInflater.from(mActivity).inflateTransition(R.transition.change_bounds);
//        sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);
//
//        sharedElementFragment2.setAllowEnterTransitionOverlap(overlap);
//        sharedElementFragment2.setAllowReturnTransitionOverlap(overlap);

        sharedElementFragment2.setSharedElementEnterTransition(new DetailsTransition());
        sharedElementFragment2.setEnterTransition(new Fade());
        //setExitTransition(new Fade());
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
