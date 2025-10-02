package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/authantification")
public class AuthantificationController {
    
    @PostMapping("/signup/email")
    public String postEmailSignup(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @PostMapping("/login/email")
    public String postEmailLogin(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
    
}
