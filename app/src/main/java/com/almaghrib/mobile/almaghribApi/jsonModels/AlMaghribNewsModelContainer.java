package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribNewsModelContainer {
    private String bannerUrl;
    private String title;
    private String author;
    private String bodyContent;
    private String videoUrl;

    public AlMaghribNewsModelContainer(String bannerUrl, String title, String author,
                                       String bodyContent, String videoUrl) {
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.author = author;
        this.bodyContent = bodyContent;
        this.videoUrl = videoUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
