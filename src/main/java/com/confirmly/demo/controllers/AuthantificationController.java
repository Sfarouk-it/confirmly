package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.DTO.LoginRequest;
import com.confirmly.demo.DTO.SigneupRequest;
import com.confirmly.demo.Services.SellerService;
import com.confirmly.demo.model.Seller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/authantification")
public class AuthantificationController {

    @Autowired
    private SellerService sellerService;

    private final AuthenticationManager authManager;

    private String appId = "1241107080824621";
    private String redirectUri = "http://localhost:8080/api/platformsAuth/facebook";

    private final String FB_OAUTH_URL = "https://www.facebook.com/v17.0/dialog/oauth";
    
    public AuthantificationController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    
    @PostMapping("/signup/email")
    public ResponseEntity<String> EmailSignup(@RequestBody SigneupRequest request) {
        
        try {
            Seller seller = sellerService.registerSeller(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.ok("Seller registered with ID: " + seller.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login/email")
    public ResponseEntity<?> EmailLogin(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (auth.isAuthenticated()) {
            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    
    @GetMapping("login/facebook")
    public void facebookLogin(HttpServletResponse response) throws java.io.IOException {
        String url = FB_OAUTH_URL +
                "?client_id=" + appId +
                "&redirect_uri=" + redirectUri +
                "&scope=pages_manage_metadata,pages_messaging";
        
        response.sendRedirect(url);
    }
    
    
}
