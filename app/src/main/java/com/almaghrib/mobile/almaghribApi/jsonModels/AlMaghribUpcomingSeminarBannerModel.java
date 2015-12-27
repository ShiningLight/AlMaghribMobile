package com.almaghrib.mobile.almaghribApi.jsonModels;

import java.io.Serializable;

public class AlMaghribUpcomingSeminarBannerModel implements Serializable {
    private String url;
    private String upcomingLocation;
    private String seminarName;
    private String seminarSubName;
    private String intructorName;
    private String date;
    private String venue;

    public AlMaghribUpcomingSeminarBannerModel(String url, String upcomingLocation,
                                               String seminarName, String seminarSubName,
                                               String intructorName, String date, String venue) {
        this.url = url;
        this.upcomingLocation = upcomingLocation;
        this.seminarName = seminarName;
        this.seminarSubName = seminarSubName;
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

    public String getSeminarSubName() {
        return seminarSubName;
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
