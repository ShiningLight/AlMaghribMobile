package com.almaghrib.mobile.facebook.jsonModels;

public class FacebookFeedPagingModel {
    String previous;
    String next;

    public FacebookFeedPagingModel(String previous, String next) {
        this.previous = previous;
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public String getNext() {
        return next;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "FacebookFeedPagingModel{" +
                "previous='" + previous + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}
