package com.almaghrib.mobile.youtube.jsonModels;

public class YouTubeSearchResultItemsModel {
    YouTubeSearchItemIdModel id;
    YouTubeSearchItemSnippetModel snippet;

    public YouTubeSearchResultItemsModel(YouTubeSearchItemIdModel id,
                                         YouTubeSearchItemSnippetModel snippet) {
        this.id = id;
        this.snippet = snippet;
    }

    public YouTubeSearchItemIdModel getId() {
        return id;
    }

    public void setId(YouTubeSearchItemIdModel id) {
        this.id = id;
    }

    public YouTubeSearchItemSnippetModel getSnippet() {
        return snippet;
    }

    public void setSnippet(YouTubeSearchItemSnippetModel snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "YouTubeSearchResultItemsModel{" +
                "id=" + id +
                ", snippet=" + snippet +
                '}';
    }
}
