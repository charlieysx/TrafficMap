package com.elims.trafficmap.bean;

/**
 * Created by elims on 16/10/21.
 */

public class VideoBean {
    private boolean isPlaying = false;
    private String place;
    private String videoUrl;

    public VideoBean(String place, String videoUrl) {
        this.place = place;
        this.videoUrl = videoUrl;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
