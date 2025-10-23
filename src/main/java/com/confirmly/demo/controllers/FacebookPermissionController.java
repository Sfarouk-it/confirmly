package com.confirmly.demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.DTO.facebookDTOs.FacebookAuthUrlResponse;
import com.confirmly.demo.Services.SellerService;
import com.confirmly.demo.model.Seller;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/facebook/permissions")
public class FacebookPermissionController {

    @Autowired
    SellerService sellerService;
    
    private String appId = "2042157466556236";
    private String redirectUri = "https://confirmly.onrender.com/api/platformsAuth/facebook/permissions";
    private final String FB_OAUTH_URL = "https://www.facebook.com/v17.0/dialog/oauth";
    
    @GetMapping("/pages")
    public ResponseEntity<?> getpermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Seller seller = sellerService.getSellerByUsername(username).get();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/facebookpermissions")
    public void testPermission(HttpServletResponse response) throws IOException  {
        String scopes = String.join(",",
                "pages_show_list",
                "pages_manage_metadata",
                "pages_read_engagement",
                "pages_manage_engagement",
                "pages_messaging"
        );

        String url = FB_OAUTH_URL +
                "?client_id=" + appId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=" + scopes;

        response.sendRedirect(url);
    }
    
}
