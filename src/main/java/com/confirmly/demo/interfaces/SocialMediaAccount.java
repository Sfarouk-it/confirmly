package com.confirmly.demo.interfaces;

public interface SocialMediaAccount {

    String getSocialId();
    String getName();
    String getPlatform();
    String getAccessToken();
    boolean isConnected();
}
