package com.almaghrib.mobile.youtube.jsonModels;

public class YouTubeSearchItemIdModel {
    String videoId;

    public YouTubeSearchItemIdModel(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return "YouTubeSearchItemIdModel{" +
                "videoId='" + videoId + '\'' +
                '}';
    }
}
