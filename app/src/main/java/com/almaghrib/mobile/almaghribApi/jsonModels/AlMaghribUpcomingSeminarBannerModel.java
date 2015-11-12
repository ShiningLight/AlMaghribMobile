package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribUpcomingSeminarBannerModel {
    protected String url;
    protected String upcomingLocation;
    protected String seminarName;
    protected String date;
    protected String venue;

    public AlMaghribUpcomingSeminarBannerModel(String url, String upcomingLocation,
                                               String seminarName, String date, String venue) {
        this.url = url;
        this.upcomingLocation = upcomingLocation;
        this.seminarName = seminarName;
        this.date = date;
        this.venue = venue;
    }

    public String getBannerUrl() {
        return url;
    }

    public String getUpcomingLocation() {
        return upcomingLocation;
    }

    public String getSeminarName() {
        return seminarName;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

}
