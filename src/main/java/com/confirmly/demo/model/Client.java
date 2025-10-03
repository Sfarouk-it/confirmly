package com.confirmly.demo.model;

import jakarta.persistence.Entity;

@Entity
public class Client extends User {
    private int loyaltyPoints;

    public Client() {}
    public Client(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
}
