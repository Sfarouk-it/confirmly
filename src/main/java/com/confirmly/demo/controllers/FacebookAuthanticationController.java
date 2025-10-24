package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.confirmly.demo.DTO.facebookDTOs.FacebookAuthUrlResponse;
import com.confirmly.demo.Services.FacebookService;
import com.confirmly.demo.config.CookieUtil;
import com.confirmly.demo.config.JwtUtil;
import com.confirmly.demo.model.Seller;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auth/facebook")
public class FacebookAuthanticationController {

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    
    RestTemplate restTemplate = new RestTemplate();
    
    @GetMapping("/authontificate")
    public ResponseEntity<?> facebookLogin() throws IOException {   
        String authUrl = facebookService.generateAuthUrl();
        return ResponseEntity.ok(new FacebookAuthUrlResponse(authUrl));
    }

    @GetMapping("/redirect")
    public void facebookAuthantification(@RequestParam String code, HttpServletResponse rsp) throws IOException {
        try {
            Seller seller = facebookService.handeleAuthCallback(code);
            String token = jwtUtil.generateToken(seller.getUsername());
            cookieUtil.createJwtCookie(rsp, token);

            
            rsp.setContentType("text/html");
            rsp.getWriter().write(
                "<script>" +
                "window.opener.postMessage({type: 'FB_LOGIN_SUCCESS', user: '" + seller.getUsername() + "'}, '*');" +
                "window.close();" +
                "</script>"
            );
        } catch (Exception e) {
            rsp.setContentType("text/html");
            rsp.getWriter().write(
                "<script>" +
                "window.opener.postMessage({type: 'FB_LOGIN_FAILED', error: '" + e.getMessage() + "'}, '*');" +
                "window.close();" +
                "</script>"
            );
        }
    }


}
