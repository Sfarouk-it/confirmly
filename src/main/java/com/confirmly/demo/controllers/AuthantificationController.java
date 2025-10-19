package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.DTO.LoginRequest;
import com.confirmly.demo.DTO.SigneupRequest;
import com.confirmly.demo.Services.SellerService;
import com.confirmly.demo.config.CookieUtil;
import com.confirmly.demo.config.JwtUtil;
import com.confirmly.demo.model.Seller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/auth")
public class AuthantificationController {

    @Autowired
    private SellerService sellerService;

    private final AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;
    
    public AuthantificationController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    
    @PostMapping("/signup")
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

    @PostMapping("/login")
    public ResponseEntity<?> EmailLogin(@RequestBody LoginRequest request , HttpServletResponse response) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (auth.isAuthenticated()) {
            String token = jwtUtil.generateToken(request.getUsername());
            cookieUtil.createJwtCookie(response, token); 

            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    
}
