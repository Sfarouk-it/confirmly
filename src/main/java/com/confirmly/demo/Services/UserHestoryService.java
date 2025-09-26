package com.confirmly.demo.Services;

import org.springframework.stereotype.Service;

@Service
public class UserHestoryService {
    
    public void getUserHistory(String userId) {
        // Logic to retrieve and return user history
        System.out.println("Retrieving history for user: " + userId);
    }
}
