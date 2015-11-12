package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribCheckInModelContainer {
    protected String qrCodeUrl;
    protected String seminarName;
    protected String date;
    protected String venue;

    public AlMaghribCheckInModelContainer(String qrCodeUrl, String seminarName, String date, String venue) {
        this.qrCodeUrl = qrCodeUrl;
        this.seminarName = seminarName;
        this.date = date;
        this.venue = venue;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
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
