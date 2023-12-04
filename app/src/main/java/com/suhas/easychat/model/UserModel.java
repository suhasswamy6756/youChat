package com.suhas.easychat.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone;
    String FCMToken;
    private String username;
    private String USerId;

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public UserModel() {
    }

    public String getUSerId() {
        return USerId;
    }

    public void setUSerId(String USerId) {
        this.USerId = USerId;
    }

    private Timestamp createdTimeStamp;

    public UserModel(String phone, String username, Timestamp createdTimeStamp,String UserId) {
        this.phone = phone;
        this.username = username;
        this.createdTimeStamp = createdTimeStamp;
        this.USerId= UserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }


}
