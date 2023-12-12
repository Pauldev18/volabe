package com.example.volunteer_campaign_management.dtos;

public class editMediaDTO{
    private int idMedia;
    private String image;
    private String video;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return idMedia;
    }

    public void setId(int id) {
        this.idMedia = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}