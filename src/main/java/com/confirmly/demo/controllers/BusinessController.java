package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.DTO.BusinessDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @PostMapping("/setup")
    public ResponseEntity<?> postMethodName(@RequestBody BusinessDTO businessDTO) {
        
        
        return new ResponseEntity<>("Business info saved successfully", null, 200);
    }
    
    
}
