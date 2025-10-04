package com.confirmly.demo.config;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {

    private final int maxAgeSeconds = 7 * 24 * 60 * 60; // 7 days

    public void createJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);         
        cookie.setSecure(true);           
        cookie.setPath("/");              
        cookie.setMaxAge(maxAgeSeconds); 
        response.addCookie(cookie);
    }

    public void clearJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);              // delete cookie immediately
        response.addCookie(cookie);
    }
}
