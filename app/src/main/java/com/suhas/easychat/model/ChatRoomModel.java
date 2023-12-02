package com.suhas.easychat.model;

import com.google.firebase.Timestamp;
import java.util.List;

public class ChatRoomModel {
    String cahtRoomID;
    List<String> userIds;
    Timestamp lastmessageTimestamp;
    String lastMessage;
    String lastMessageSenderId;

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public ChatRoomModel() {
    }

    public ChatRoomModel(String cahtRoomID, List<String> userIds, Timestamp lastmessageTimestamp, String lastMessageSenderId) {
        this.cahtRoomID = cahtRoomID;
        this.userIds = userIds;
        this.lastmessageTimestamp = lastmessageTimestamp;

        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getCahtRoomID() {
        return cahtRoomID;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public Timestamp getLastmessageTimestamp() {
        return lastmessageTimestamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setCahtRoomID(String cahtRoomID) {
        this.cahtRoomID = cahtRoomID;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public void setLastmessageTimestamp(Timestamp lastmessageTimestamp) {
        this.lastmessageTimestamp = lastmessageTimestamp;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
