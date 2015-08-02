package com.almaghrib.mobile.facebook.ui;

import java.util.List;

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
import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.login.widget.ProfilePictureView;

public class FacebookFeedListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private String mUserId;
    private List<FacebookFeedItem> mFeedItems;
    private ImageLoader mImageLoader;

    public FacebookFeedListAdapter(Context context, List<FacebookFeedItem> feedItems) {
        mFeedItems = feedItems;
        mInflater = LayoutInflater.from(context);
        mImageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return mFeedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return mFeedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FacebookFeedViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.facebook_feed_item, null);

            holder = new FacebookFeedViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            holder.statusMessage = (TextView) convertView.findViewById(R.id.txtStatusMsg);
            holder.url = (TextView) convertView.findViewById(R.id.txtUrl);
            holder.profilePic = (ProfilePictureView) convertView.findViewById(R.id.profilePic);
            holder.image = (FeedImageView) convertView.findViewById(R.id.feedImage1);

            // store the holder with the view
            convertView.setTag(holder);
        } else {
            holder = (FacebookFeedViewHolder) convertView.getTag();
        }

        final FacebookFeedItem item = mFeedItems.get(position);

        holder.name.setText(item.getName());

        // Converting timestamp into x ago format
        /*CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);*/
        holder.timestamp.setText(item.getTimeStamp());//timeAgo);

        // Check for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            holder.statusMessage.setText(item.getStatus());
            holder.statusMessage.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            holder.statusMessage.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getUrl() != null) {
            holder.url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));
            // Making url clickable
            holder.url.setMovementMethod(LinkMovementMethod.getInstance());
            holder.url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            holder.url.setVisibility(View.GONE);
        }

        // set user profile pic by setting the user id - facebook view will do the retrieval
        holder.profilePic.setProfileId(mUserId);

        // Feed image
        if (item.getImage() != null) {
            holder.image.setImageUrl(item.getImage(), mImageLoader);
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    /**
     * Update adapter with new list of feedItems
     * @param feedItems
     */
    public void updateAdapter(List<FacebookFeedItem> feedItems) {
        mFeedItems = feedItems;
        notifyDataSetChanged();
    }

}