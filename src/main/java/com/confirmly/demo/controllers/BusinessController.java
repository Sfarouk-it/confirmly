package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.DTO.BusinessDTO;
import com.confirmly.demo.Services.BusinessService;
import com.confirmly.demo.config.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/setup")
    public ResponseEntity<?> postMethodName(@RequestBody BusinessDTO businessDTO, HttpServletRequest request) {
        
        String username = jwtUtil.extractUsernameFromRequest(request);

        if (username == null) {
            return ResponseEntity.status(401).body("Unauthorized: missing or invalid JWT");
        }

        businessService.saveBusiness(businessDTO, username);
        return ResponseEntity.ok("Business info saved successfully");
    }
    
    
}
