package com.confirmly.demo.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "facebook_account")
public class FacebookAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String facebookId;
    
    private String name;
    private String email;
    private String accessToken;
    private String longLivedToken;
    private Long tokenExpiresAt;
    private boolean connected;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "facebookAccount")
    private List<FacebookPage> pages; 

    public FacebookAccount() {}

    public FacebookAccount(String facebookId, String name, String accessToken, boolean connected) {
        this.facebookId = facebookId;
        this.name = name;
        this.accessToken = accessToken;
        this.connected = connected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLongLivedToken() {
        return longLivedToken;
    }

    public void setLongLivedToken(String longLivedToken) {
        this.longLivedToken = longLivedToken;
    }

    public Long getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public void setTokenExpiresAt(Long tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }


    public void setSocialId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getSocialId() {
        return facebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return "Facebook";
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isConnected() {
        return connected;
    }
    
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public List<FacebookPage> getPages() {
        return pages;
    }

    public void setPages(List<FacebookPage> pages) {
        this.pages = pages;
    }

    public void addPage(FacebookPage page) {
        this.pages.add(page);
        page.setFacebookAccount(this);
    }
}
