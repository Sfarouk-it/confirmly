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
import com.confirmly.demo.Repositories.SellerRepository;
import com.confirmly.demo.Services.FacebookService;
import com.confirmly.demo.model.Seller;
import com.confirmly.demo.DTO.FacebookAuthResponse;
import com.confirmly.demo.DTO.FacebookAuthUrlResponse;
import com.confirmly.demo.DTO.FacebookMessageRequest;
import com.confirmly.demo.DTO.FacebookPageDto;


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
        
        String authUrl = facebookService.generateAuthUrl(user.getId());
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
    
    @GetMapping("/pages")
    public ResponseEntity<List<FacebookPageDto>> getUserPages(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Seller user = sellerRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<FacebookPageDto> pages = facebookService.getUserPages(user.getId());
        return ResponseEntity.ok(pages);
    }
    
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
    
    @DeleteMapping("/disconnect")
    public ResponseEntity<?> disconnectFacebook(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Seller user = sellerRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            facebookService.disconnectFacebook(user.getId());
            return ResponseEntity.ok("Facebook account disconnected successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}