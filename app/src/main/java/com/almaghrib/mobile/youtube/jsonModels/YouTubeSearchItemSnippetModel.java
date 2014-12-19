package com.almaghrib.mobile.youtube.jsonModels;

public class YouTubeSearchItemSnippetModel {
    String publishedAt;
    String title;
    YouTubeSearchItemThumbnailsModel thumbnails;

    public YouTubeSearchItemSnippetModel(String publishedAt, String title,
                                         YouTubeSearchItemThumbnailsModel thumbnails) {
        this.publishedAt = publishedAt;
        this.title = title;
        this.thumbnails = thumbnails;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public YouTubeSearchItemThumbnailsModel getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(YouTubeSearchItemThumbnailsModel thumbnails) {
        this.thumbnails = thumbnails;
    }

    @Override
    public String toString() {
        return "YouTubeSearchItemSnippetModel{" +
                "publishedAt='" + publishedAt + '\'' +
                ", title='" + title + '\'' +
                ", thumbnails=" + thumbnails +
                '}';
    }
}
