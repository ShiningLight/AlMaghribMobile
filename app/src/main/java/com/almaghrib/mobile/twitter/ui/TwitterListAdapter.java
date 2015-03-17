package com.almaghrib.mobile.twitter.ui;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.facebook.data.FacebookFeedItem;
import com.almaghrib.mobile.twitter.data.Twitter;
import com.almaghrib.mobile.twitter.jsonModels.Tweet;
import com.almaghrib.mobile.util.DateUtils;
import com.almaghrib.mobile.util.view.FeedImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.SimpleDateFormat;
import java.util.List;

public class TwitterListAdapter extends BaseAdapter {
    private static final SimpleDateFormat RETRIEVED_DATE_FORMAT =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT =
            new SimpleDateFormat("d MMM yyyy 'at' HH:mm");

    private LayoutInflater mInflater;
    private Twitter feedItems;
    private ImageLoader mImageLoader;

    public TwitterListAdapter(Context context, Twitter feedItems) {
        this.feedItems = feedItems;
        this.mInflater = LayoutInflater.from(context);
        this.mImageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwitterViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.twitter_timeline_item, null);

            holder = new TwitterViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            holder.statusMessage = (TextView) convertView.findViewById(R.id.txtStatusMsg);
            holder.url = (TextView) convertView.findViewById(R.id.txtUrl);
            holder.profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
            holder.image = (FeedImageView) convertView.findViewById(R.id.feedImage1);

            // store the holder with the view
            convertView.setTag(holder);
        } else {
            holder = (TwitterViewHolder) convertView.getTag();
        }

        final Tweet item = feedItems.get(position);

        holder.name.setText("@" + item.getUser().getScreenName());

        final String formattedTime = DateUtils.getFormattedDate(
                RETRIEVED_DATE_FORMAT, OUTPUT_DATE_FORMAT, item.getDateCreated());
        holder.timestamp.setText(formattedTime);

        // Check for empty status message
        if (!TextUtils.isEmpty(item.getText())) {
            holder.statusMessage.setText(item.getText());
            holder.statusMessage.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            holder.statusMessage.setVisibility(View.GONE);
        }

        holder.url.setVisibility(View.GONE);
        // user profile pic
        holder.profilePic.setDefaultImageResId(R.drawable.facebook_profile_pic_placeholder);
        holder.profilePic.setImageUrl(item.getUser().getProfileImageUrl(), mImageLoader);

        // Feed image
//        if (item.getImage() != null) {
//            holder.image.setImageUrl(item.getImage(), mImageLoader);
//            holder.image.setVisibility(View.VISIBLE);
//        } else {
//            holder.image.setVisibility(View.GONE);
//        }
        holder.image.setVisibility(View.GONE);
        return convertView;
    }

    /**
     * Update adapter with new list of feedItems
     *
     * @param feedItems
     */
    public void updateAdapter(Twitter feedItems) {
        this.feedItems = feedItems;
        notifyDataSetChanged();
    }

}