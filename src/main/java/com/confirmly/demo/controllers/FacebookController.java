package com.confirmly.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.DTO.facebookDTOs.FacebookAuthResponse;
import com.confirmly.demo.DTO.facebookDTOs.FacebookAuthUrlResponse;
import com.confirmly.demo.DTO.facebookDTOs.FacebookMessageRequest;
import com.confirmly.demo.DTO.facebookDTOs.FacebookPageDTO;
import com.confirmly.demo.Repositories.SellerRepository;
import com.confirmly.demo.Services.FacebookService;
import com.confirmly.demo.model.Seller;


@RestController
@RequestMapping("/api/facebook")
public class FacebookController {
    
    @Autowired
    private FacebookService facebookService;
    
    @Autowired
    private SellerRepository sellerRepository;
    
    @GetMapping("/auth-url")
    public ResponseEntity<FacebookAuthUrlResponse> getAuthUrl(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Seller user = sellerRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String authUrl = facebookService.generatePermissionsUrl(user.getId());
        return ResponseEntity.ok(new FacebookAuthUrlResponse(authUrl));
    }
    
    @GetMapping("/callback")
    public ResponseEntity<?> handleCallback(
            @RequestParam String code,
            @RequestParam String state) {
        try {
            Long userId = Long.parseLong(state);
            FacebookAuthResponse response = facebookService.handleCallback(code, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /* 
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(
            @RequestBody FacebookMessageRequest request,
            Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Seller user = sellerRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            String response = facebookService.sendMessage(user.getId(), request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
        */
}