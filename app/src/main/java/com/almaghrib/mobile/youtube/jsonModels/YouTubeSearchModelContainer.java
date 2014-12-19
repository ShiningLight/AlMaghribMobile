package com.almaghrib.mobile.youtube.jsonModels;

import java.util.ArrayList;

public class YouTubeSearchModelContainer {
    String etag;
    String nextPageToken;
    YouTubeSearchPageInfoModel pageInfo;
    ArrayList<YouTubeSearchResultItemsModel> items;
    String channelTitle;

    public YouTubeSearchModelContainer(String etag, String nextPageToken,
                                       ArrayList<YouTubeSearchResultItemsModel> items,
                                       YouTubeSearchPageInfoModel pageInfo,
                                       String channelTitle) {
        this.etag = etag;
        this.nextPageToken = nextPageToken;
        this.items = items;
        this.pageInfo = pageInfo;
        this.channelTitle = channelTitle;
    }

    public String getEtag() {
        return etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public ArrayList<YouTubeSearchResultItemsModel> getItems() {
        return items;
    }

    public YouTubeSearchPageInfoModel getPageInfo() {
        return pageInfo;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void setPageInfo(YouTubeSearchPageInfoModel pageInfo) {
        this.pageInfo = pageInfo;
    }

    public void setItems(ArrayList<YouTubeSearchResultItemsModel> items) {
        this.items = items;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    @Override
    public String toString() {
        return "YouTubeSearchModelContainer{" +
                "etag='" + etag + '\'' +
                ", nextPageToken='" + nextPageToken + '\'' +
                ", pageInfo=" + pageInfo.toString() +
                ", items=" + items.toString() +
                ", channelTitle='" + channelTitle + '\'' +
                '}';
    }
}
