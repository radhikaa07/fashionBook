package com.example.journalapp;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Journal {
    private String title;
    private String thoughts;
    private String imageUrl;

    private String userId;
    private Timestamp timestamp;
    private String userName;

    public Journal() {
    }

    public Journal(String title, String thoughts, String imageUrl, String userId, Timestamp timestamp, String userName) {
        this.title = title;
        this.thoughts = thoughts;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timestamp = timestamp;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
