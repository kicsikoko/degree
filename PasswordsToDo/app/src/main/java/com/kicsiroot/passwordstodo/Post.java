package com.kicsiroot.passwordstodo;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postKey;
    private String addUrl;
    private String addEmail;
    private String addUsername;
    private String addPassword;
    private String userID;
    private Object timeStamp;


    public Post(String addUrl, String addEmail, String addUsername, String addPassword, String userID, Object timeStamp) {
        this.addUrl = addUrl;
        this.addEmail = addEmail;
        this.addUsername = addUsername;
        this.addPassword = addPassword;
        this.userID = userID;
        this.timeStamp = ServerValue.TIMESTAMP;
    }


    public Post() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getAddUrl() {
        return addUrl;
    }

    public String getAddEmail() {
        return addEmail;
    }

    public String getAddUsername() {
        return addUsername;
    }

    public String getAddPassword() {
        return addPassword;
    }

    public String getUserID() {
        return userID;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setAddUrl(String addUrl) {
        this.addUrl = addUrl;
    }

    public void setAddEmail(String addEmail) {
        this.addEmail = addEmail;
    }

    public void setAddUsername(String addUsername) {
        this.addUsername = addUsername;
    }

    public void setAddPassword(String addPassword) {
        this.addPassword = addPassword;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
