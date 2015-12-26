package com.almaghrib.mobile;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almaghrib.mobile.instructors.jsonModels.InstructorModelContainer;
import com.almaghrib.mobile.util.view.CircleNetworkImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class InstructorViewPagerAdapter extends PagerAdapter {

    final Context mContext;
    final LayoutInflater mLayoutInflater;
    final ImageLoader mImageLoader;
    final List<InstructorModelContainer> mInstructorList;

    public InstructorViewPagerAdapter(Context context, List<InstructorModelContainer> instructorList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = RequestQueueSingleton.getInstance(mContext.getApplicationContext()).getImageLoader();
        mInstructorList = instructorList;
    }

    @Override
    public int getCount() {
        return mInstructorList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.instructor_view_pager_item, container, false);

        final InstructorModelContainer instructorModel = mInstructorList.get(position);

        final CircleNetworkImageView imageView = (CircleNetworkImageView) itemView.findViewById(R.id.profile_image);
        imageView.setDefaultImageResId(R.drawable.waleed);
        //imageView.setImageUrl(instructorModel.getPhotoUrl(), mImageLoader);

        final TextView nameTextView = (TextView) itemView.findViewById(R.id.instructorNameTextView);
        nameTextView.setText(instructorModel.getName());

        final TextView roleTextView = (TextView) itemView.findViewById(R.id.instructorRoleTextView);
        roleTextView.setText(instructorModel.getRole());

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}