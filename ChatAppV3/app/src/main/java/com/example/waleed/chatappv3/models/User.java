package com.example.waleed.chatappv3.models;

import java.io.Serializable;

public class User implements Serializable{
    private String userUID;
    private String userName;
    private String userEmail;
    private String connectionState;
    private String profileImageUrl;
    private String conversations;

    public User(String userUID, String userName, String profileImageUrl, String userEmail, String connectionState,String conversations) {
        this.userUID=userUID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.profileImageUrl=profileImageUrl;
        this.connectionState = connectionState;
        this.conversations=conversations;
    }
    public User(){}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setconnectionState(String connectionState) {
        this.connectionState = connectionState;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getConversations() {
        return conversations;
    }

    public void setConversations(String conversations) {
        this.conversations = conversations;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getconnectionState() {
        return connectionState;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
