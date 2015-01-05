package com.almaghrib.mobile.facebook.jsonModels;

import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchPageInfoModel;
import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchResultItemsModel;

import java.util.ArrayList;

public class FacebookFeedModelContainer {
    ArrayList<FacebookFeedDataModel> data;
    FacebookFeedPagingModel paging;

    public FacebookFeedModelContainer(ArrayList<FacebookFeedDataModel> data,
                                      FacebookFeedPagingModel paging) {
        this.data = data;
        this.paging = paging;
    }

    public ArrayList<FacebookFeedDataModel> getData() {
        return data;
    }

    public FacebookFeedPagingModel getPagingInfo() {
        return paging;
    }

    public void setData(ArrayList<FacebookFeedDataModel> data) {
        this.data = data;
    }

    public void setPaging(FacebookFeedPagingModel paging) {
        this.paging = paging;
    }

    @Override
    public String toString() {
        return "FacebookFeedModelContainer{" +
                "data=" + ((data != null) ? data.toString() : null) +
                ", paging=" + ((paging != null) ? paging.toString() : null) +
                '}';
    }
}
