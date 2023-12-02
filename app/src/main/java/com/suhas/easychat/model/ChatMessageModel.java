package com.suhas.easychat.model;
import com.google.firebase.Timestamp;
public class ChatMessageModel {
    private Timestamp timestamp;
    private String message;
    private String SenderId;

    public ChatMessageModel() {
    }

    public ChatMessageModel(Timestamp timestamp, String message, String userId) {
        this.timestamp = timestamp;
        this.message = message;
        this.SenderId = userId;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.SenderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return SenderId;
    }
}
