package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.confirmly.demo.Services.SellerService;
import com.confirmly.demo.config.CookieUtil;
import com.confirmly.demo.config.JwtUtil;
import com.confirmly.demo.model.Seller;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/platformsAuth")
public class PlatformsCallBack {

    @Autowired
    SellerService sellerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;


    private String appId = "1241107080824621";
    private String appSecret = "2e87dd551c7b456dbd5d9db8a139297b";
    private String redirectUri = "https://confirmly.onrender.com/api/platformsAuth/facebook";
    private final String FB_TOKEN_URL = "https://graph.facebook.com/v17.0/oauth/access_token";

    @GetMapping("/facebook")
    public ResponseEntity<?> facebookAuthantification(@RequestParam String code ,HttpServletResponse rsp) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenRequestUrl = FB_TOKEN_URL +
                "?client_id=" + appId +
                "&redirect_uri=" + redirectUri +
                "&client_secret=" + appSecret +
                "&code=" + code;

        ResponseEntity<Map> response = restTemplate.getForEntity(tokenRequestUrl, Map.class);
        String accessToken = (String) response.getBody().get("access_token");

        // Get user info from Facebook
        String userInfoUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
        Map<String, Object> fbUser = userInfoResponse.getBody();

        String facebookId = (String) fbUser.get("id");
        String name = (String) fbUser.get("name");
        String email = (String) fbUser.get("email");

        // Generate internal username
        String username = "fb_" + facebookId;

        try {
            Seller seller = sellerService.registerSellerbyFacebook(username, email, facebookId);
            String token = jwtUtil.generateToken(seller.getUsername());
            cookieUtil.createJwtCookie(rsp, token);

            rsp.sendRedirect("https://confirmlydz.vercel.app/dashboard");
            return null;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
}
