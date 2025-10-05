package com.confirmly.demo.config;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {

    private final int maxAgeSeconds = 7 * 24 * 60 * 60; // 7 days

    public void createJwtCookie(HttpServletResponse response, String token) {
        String header = String.format(
            "jwt=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=None",
            token, maxAgeSeconds
        );
        response.addHeader("Set-Cookie", header);
    }

    public void clearJwtCookie(HttpServletResponse response) {
        String header = "jwt=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=None";
        response.addHeader("Set-Cookie", header);
    }
}
