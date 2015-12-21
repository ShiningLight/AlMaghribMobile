package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribUpcomingSeminarBannerModel {
    private String url;
    private String upcomingLocation;
    private String seminarName;
    private String intructorName;
    private String date;
    private String venue;

    public AlMaghribUpcomingSeminarBannerModel(String url, String upcomingLocation,
                                               String seminarName, String intructorName, String date, String venue) {
        this.url = url;
        this.upcomingLocation = upcomingLocation;
        this.seminarName = seminarName;
        this.intructorName = intructorName;
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

    public String getInstructorName() {
        return intructorName;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

}
