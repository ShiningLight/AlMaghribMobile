package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribMyAccountsOrderHistoryModel {
    protected String seminarName;
    protected String info;
    protected String price;

    public AlMaghribMyAccountsOrderHistoryModel(String seminarName, String info, String price) {
        this.seminarName = seminarName;
        this.info = info;
        this.price = price;
    }

    public String getSeminarName() {
        return seminarName;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }
}
