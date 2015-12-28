package com.almaghrib.mobile.almaghribApi.jsonModels;

import java.util.List;

public class AlMaghribUpcomingSeminarsLocationListModelContainer {
    private String bannerUrl;
    private String title;
    private List<String> cities;

    public AlMaghribUpcomingSeminarsLocationListModelContainer(String bannerUrl, String title, List<String> cities) {
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.cities = cities;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getCities() {
        return cities;
    }
}
