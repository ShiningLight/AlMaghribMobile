package com.almaghrib.mobile.facebook.jsonModels;

public class FacebookFeedItemFromModel {
    String name;
    String id;

    public FacebookFeedItemFromModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FacebookFeedItemFromModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
