package com.almaghrib.mobile.youtube.jsonModels;

import com.google.gson.annotations.SerializedName;

public class YouTubeSearchItemThumbnailsModel {
    @SerializedName("default")
    YouTubeSearchItemThumbnailDensityModel defaultThumbnail;
    YouTubeSearchItemThumbnailDensityModel medium;
    YouTubeSearchItemThumbnailDensityModel high;

    public YouTubeSearchItemThumbnailsModel(YouTubeSearchItemThumbnailDensityModel defaultThumbnail,
                                            YouTubeSearchItemThumbnailDensityModel medium,
                                            YouTubeSearchItemThumbnailDensityModel high) {
        this.defaultThumbnail = defaultThumbnail;
        this.medium = medium;
        this.high = high;
    }

    public YouTubeSearchItemThumbnailDensityModel getDefaultThumbnail() {
        return defaultThumbnail;
    }

    public void setDefaultThumbnail(YouTubeSearchItemThumbnailDensityModel defaultThumbnail) {
        this.defaultThumbnail = defaultThumbnail;
    }

    public YouTubeSearchItemThumbnailDensityModel getMedium() {
        return medium;
    }

    public void setMedium(YouTubeSearchItemThumbnailDensityModel medium) {
        this.medium = medium;
    }

    public YouTubeSearchItemThumbnailDensityModel getHigh() {
        return high;
    }

    public void setHigh(YouTubeSearchItemThumbnailDensityModel high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "YouTubeSearchItemThumbnailsModel{" +
                "defaultThumbnail=" + defaultThumbnail +
                ", medium=" + medium +
                ", high=" + high +
                '}';
    }
}
