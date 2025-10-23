package com.confirmly.demo.model;

import com.confirmly.demo.interfaces.SocialMediaAccount;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class FacebookPage implements SocialMediaAccount {
    
    @Id
    private Long id;
    private String pageId;
    private String pageName;
    private String pageAccessToken;

    @ManyToOne
    @JoinColumn(name = "facebook_account_id")
    private FacebookAccount facebookAccount;

    private String category;
    private String[] tasks;

    public FacebookPage(String pageId, String pageName, String pageAccessToken) {
        this.pageId = pageId;
        this.pageName = pageName;
        this.pageAccessToken = pageAccessToken;
    }

    public FacebookPage() {
        //TODO Auto-generated constructor stub
    }

    @Override
    public String getSocialId() {
        return pageId;
    }

    @Override
    public String getName() {
        return pageName;
    }

    @Override
    public String getPlatform() {
        return "Facebook Page";
    }

    @Override
    public String getAccessToken() {
        return pageAccessToken;
    }

    @Override
    public boolean isConnected() {
        return pageAccessToken != null && !pageAccessToken.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageAccessToken() {
        return pageAccessToken;
    }

    public void setPageAccessToken(String pageAccessToken) {
        this.pageAccessToken = pageAccessToken;
    }

    public FacebookAccount getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(FacebookAccount facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getTasks() {
        return tasks;
    }

    public void setTasks(String[] tasks) {
        this.tasks = tasks;
    }
    
}
