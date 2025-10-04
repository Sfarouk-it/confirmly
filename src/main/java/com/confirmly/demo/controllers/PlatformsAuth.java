package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/platformsAuth")
public class PlatformsAuth {

    private String appId = "1241107080824621";
    private String appSecret = "2e87dd551c7b456dbd5d9db8a139297b";
    private String redirectUri = "https://confirmly.onrender.com/api/platformsAuth/facebook";
    private final String FB_TOKEN_URL = "https://graph.facebook.com/v17.0/oauth/access_token";

    @GetMapping("/facebook")
    public String facebookAuthantification(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();

        String tokenRequestUrl = FB_TOKEN_URL +
                "?client_id=" + appId +
                "&redirect_uri=" + redirectUri +
                "&client_secret=" + appSecret +
                "&code=" + code;

        ResponseEntity<String> response = restTemplate.getForEntity(tokenRequestUrl, String.class);

        System.out.println(response.getBody());
        
        return "Access Token Response: " + response.getBody();
    }
    
}
