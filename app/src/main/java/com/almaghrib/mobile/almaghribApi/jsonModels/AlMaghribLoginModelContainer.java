package com.almaghrib.mobile.almaghribApi.jsonModels;

import com.almaghrib.mobile.youtube.jsonModels.YouTubeSearchModelContainer;

public class AlMaghribLoginModelContainer {
    String token;
    AlMaghribLoginItemUserModel user;


    public AlMaghribLoginModelContainer(String token, AlMaghribLoginItemUserModel user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public AlMaghribLoginItemUserModel getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "AlMaghribLoginModelContainer{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
