package com.confirmly.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller extends User {

    private String username;
    private String email;
    private String password;
    private String facebookId;
    
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Business> businesses = new ArrayList<>();

    public Seller() {}
    public Seller(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFacebookId() { return facebookId; }
    public void setFacebookId(String facebookId) { this.facebookId = facebookId; }
    public List<Business> getBusinesses() { return businesses; }
    public void setBusinesses(List<Business> businesses) { this.businesses = businesses; }
    public void addBusiness(Business business) {
        businesses.add(business);
        business.setSeller(this);
    }
    public void removeBusiness(Business business) {
        businesses.remove(business);
        business.setSeller(null);
    }
}
