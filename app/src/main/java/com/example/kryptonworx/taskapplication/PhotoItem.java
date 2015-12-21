package com.example.kryptonworx.taskapplication;

/**
 * Created by kryptonWorx on 20-Dec-15.
 */
public class PhotoItem {

    String title;
    String thumbnail;
    String owner;
    String farm;
    String id;
    String secret;
    String server;

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
