package com.confirmly.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Client extends User {

    private int loyaltyPoints;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Messages> messages;

    public Client() {}

    public Client(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }
}
