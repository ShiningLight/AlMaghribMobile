package com.almaghrib.mobile.facebook.jsonModels;

public class FacebookFeedDataModel {
    String id;
    FacebookFeedItemFromModel from;
    String message;
    String story;
    String picture;
    String link;
    String caption;
    String name;
    String type;
    String created_time;

    public FacebookFeedDataModel(String id, FacebookFeedItemFromModel from, String message,
                                 String story, String picture, String link, String caption,
                                 String name, String type, String created_time) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.story = story;
        this.picture = picture;
        this.link = link;
        this.caption = caption;
        this.name = name;
        this.type = type;
        this.created_time = created_time;
    }

    public String getId() {
        return id;
    }

    public FacebookFeedItemFromModel getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getStory() {
        return story;
    }

    public String getPicture() {
        return picture;
    }

    public String getLink() {
        return link;
    }

    public String getCaption() {
        return caption;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFrom(FacebookFeedItemFromModel from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    @Override
    public String toString() {
        return "FacebookFeedDataModel{" +
                "id='" + id + '\'' +
                ", from=" + from.toString() +
                ", message='" + message + '\'' +
                ", story='" + story + '\'' +
                ", picture='" + picture + '\'' +
                ", link='" + link + '\'' +
                ", caption='" + caption + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", created_time='" + created_time + '\'' +
                '}';
    }
}
