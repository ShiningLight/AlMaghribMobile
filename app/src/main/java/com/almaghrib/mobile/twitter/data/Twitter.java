package com.almaghrib.mobile.twitter.data;

import com.almaghrib.mobile.twitter.jsonModels.Tweet;

import java.util.ArrayList;

// a collection of tweets
public class Twitter extends ArrayList<Tweet> {
    private static final long serialVersionUID = 1L;
    private String userScreenName;

    public void setScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    public String getScreenName() {
        return this.userScreenName;
    }
}