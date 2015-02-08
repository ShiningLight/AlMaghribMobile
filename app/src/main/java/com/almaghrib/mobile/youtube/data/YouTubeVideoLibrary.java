package com.almaghrib.mobile.youtube.data;

import java.io.Serializable;
import java.util.List;
 
/**
 * This is the 'library' of all the users videos
 *
 */
public class YouTubeVideoLibrary implements Serializable{
    private static final long serialVersionUID = 5269684266359946122L;
    
    // The username of the owner of the library
    private String user;
    // A list of videos that the user owns
    private List<YouTubeVideo> videos;
     
    public YouTubeVideoLibrary(String user, List<YouTubeVideo> videos) {
        this.user = user;
        this.videos = videos;
    }
 
    /**
     * @return the user name
     */
    public String getUser() {
        return user;
    }
 
    /**
     * @return the videos
     */
    public List<YouTubeVideo> getVideos() {
        return videos;
    }

    public void addVideos(List<YouTubeVideo> videos) {
        this.videos.addAll(videos);
    }
}