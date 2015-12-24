package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribJournalModelContainer {
    private String bannerUrl;
    private String title;

    public AlMaghribJournalModelContainer(String bannerUrl, String title) {
        this.bannerUrl = bannerUrl;
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getTitle() {
        return title;
    }

}
