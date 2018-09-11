package com.example.waleed.chatappv3.models;

public class Message {
    private User sendBy;
    private User sendTo;
    private String textMessage;

    public Message(User sendBy, User sendTo, String textMessage) {
        this.sendBy = sendBy;
        this.sendTo = sendTo;
        this.textMessage = textMessage;
    }

    public User getSendBy() {
        return sendBy;
    }

    public void setSendBy(User sendBy) {
        this.sendBy = sendBy;
    }

    public User getSendTo() {
        return sendTo;
    }

    public void setSendTo(User sendTo) {
        this.sendTo = sendTo;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
