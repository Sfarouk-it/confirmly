package com.confirmly.demo.controllers;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/permissions")
public class GetPermissionController {

    private String appId = "1241107080824621";
    private String redirectUri = "https://confirmly.onrender.com/api/platformsAuth/facebook";
    private final String FB_OAUTH_URL = "https://www.facebook.com/v17.0/dialog/oauth";
    
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
