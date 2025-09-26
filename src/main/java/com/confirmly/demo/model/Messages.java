package com.confirmly.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Messages {
    
    @Id
    private String id;
    private String senderId;
    private String text;
    private String timestamp;

    private Client client;

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

    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
}
