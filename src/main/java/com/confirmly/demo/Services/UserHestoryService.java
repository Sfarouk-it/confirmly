package com.confirmly.demo.Services;

import org.springframework.stereotype.Service;

@Service
public class UserHestoryService {
    
    public void getUserHistory(String userId) {
        System.out.println("Retrieving history for user: " + userId);
    }
}
