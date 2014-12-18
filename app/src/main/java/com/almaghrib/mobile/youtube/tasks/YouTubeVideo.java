package com.almaghrib.mobile.youtube.tasks;

import java.io.Serializable;

/**
 * This is a representation of a users video off YouTube
 */
public class YouTubeVideo implements Serializable {
    private static final long serialVersionUID = -5723456090357535775L;
    
    // The title of the video
    private String title;
    // A link to the video on youtube
    private String url;
    // A link to a still image of the youtube video
    private String thumbUrl;
     
    public YouTubeVideo(String title, String url, String thumbUrl) {
        this.title = title;
        this.url = url;
        this.thumbUrl = thumbUrl;
    }
 
    /**
     * @return the title of the video
     */
    public String getTitle(){
        return title;
    }
 
    /**
     * @return the url to this video on youtube
     */
    public String getUrl() {
        return url;
    }
 
    /**
     * @return the thumbUrl of a still image representation of this video
     */
    public String getThumbUrl() {
        return thumbUrl;
    }
    
}
