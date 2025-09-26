package com.confirmly.demo.model;

import jakarta.persistence.Entity;

@Entity
public class Messages {
    
    private String id;
    private String senderId;
    private String text;
    private String timestamp;

    public Messages() {}

    public Messages(String id, String senderId, String text, String timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
}
