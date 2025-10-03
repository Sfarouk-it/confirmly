package com.confirmly.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller extends User {

    private String username;
    private String email;
    private String password;
    private String shopName;

    public Seller() {}
    public Seller(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
