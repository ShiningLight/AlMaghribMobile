package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribMyAccountsExamHistoryModel {
    protected String seminarName;
    protected String info;
    protected String mark;

    public AlMaghribMyAccountsExamHistoryModel(String seminarName, String info, String mark) {
        this.seminarName = seminarName;
        this.info = info;
        this.mark = mark;
    }

    public String getSeminarName() {
        return seminarName;
    }

    public String getInfo() {
        return info;
    }

    public String getMark() {
        return mark;
    }
}
