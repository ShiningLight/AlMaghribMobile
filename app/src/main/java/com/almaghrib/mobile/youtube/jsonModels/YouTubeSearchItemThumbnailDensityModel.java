package com.almaghrib.mobile.youtube.jsonModels;

public class YouTubeSearchItemThumbnailDensityModel {
    String url;

    public YouTubeSearchItemThumbnailDensityModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "YouTubeSearchItemThumbnailDensityModel{" +
                "url='" + url + '\'' +
                '}';
    }
}
