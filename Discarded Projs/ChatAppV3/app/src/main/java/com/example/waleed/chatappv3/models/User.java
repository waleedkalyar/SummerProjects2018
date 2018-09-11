package com.example.waleed.chatappv3.models;

public class User {
    private String userName;
    private String userEmail;
    private String connectionState;
    private String profileImageUrl;

    public User(String userName, String profileImageUrl, String userEmail, String connectionState) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.profileImageUrl=profileImageUrl;
        this.connectionState = connectionState;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setconnectionState(String connectionState) {
        this.connectionState = connectionState;
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
