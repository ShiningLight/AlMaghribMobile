package com.almaghrib.mobile.facebook.ui;

import android.widget.TextView;

import com.almaghrib.mobile.util.view.FeedImageView;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.login.widget.ProfilePictureView;

public class FacebookFeedViewHolder {
    ProfilePictureView profilePic;
    TextView name;
    TextView timestamp;
    TextView statusMessage;
    TextView url;
    FeedImageView image;
}
