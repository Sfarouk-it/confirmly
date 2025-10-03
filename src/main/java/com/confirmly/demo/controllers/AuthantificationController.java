package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.DTO.SigneupRequest;
import com.confirmly.demo.Services.SellerService;
import com.confirmly.demo.model.Seller;

import org.springframework.security.authentication.AuthenticationManager;
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
    public String EmailLogin(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
    
}
