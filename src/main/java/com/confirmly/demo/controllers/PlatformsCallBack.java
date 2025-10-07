package com.confirmly.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.confirmly.demo.Services.SellerService;
import com.confirmly.demo.config.CookieUtil;
import com.confirmly.demo.config.JwtUtil;
import com.confirmly.demo.model.Seller;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/platformsAuth")
public class PlatformsCallBack {

    @Autowired
    SellerService sellerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;


    private String appId = "2042157466556236";
    private String appSecret = "85ae7b1fcc58c212feea85ef9399be8c";
    private String redirectUri = "https://confirmly.onrender.com/api/platformsAuth/facebook";
    private final String FB_TOKEN_URL = "https://graph.facebook.com/v17.0/oauth/access_token";
    private final String FB_ME_ACCOUNTS_URL = "https://graph.facebook.com/v17.0/me/accounts";
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/facebook")
    public ResponseEntity<?> facebookAuthantification(@RequestParam String code ,HttpServletResponse rsp) {
        String tokenRequestUrl = FB_TOKEN_URL +
                "?client_id=" + appId +
                "&redirect_uri=" + redirectUri +
                "&client_secret=" + appSecret +
                "&code=" + code;

        ResponseEntity<Map> response = restTemplate.getForEntity(tokenRequestUrl, Map.class);
        String accessToken = (String) response.getBody().get("access_token");

        // Get user info from Facebook
        String userInfoUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
        Map<String, Object> fbUser = userInfoResponse.getBody();

        String facebookId = (String) fbUser.get("id");
        String name = (String) fbUser.get("name");
        String email = (String) fbUser.get("email");

        // Generate internal username
        String username = "fb_" + facebookId;

        try {
            Seller seller = sellerService.registerSellerbyFacebook(username, email, facebookId);
            String token = jwtUtil.generateToken(seller.getUsername());
            cookieUtil.createJwtCookie(rsp, token);

            rsp.sendRedirect("https://confirmlydz.vercel.app/accountsetup");
            return null;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }


    public ResponseEntity<?> handleFacebookRedirect(@RequestParam(name = "code", required = false) String code,
                                                    @RequestParam(name = "error", required = false) String error) {

        if (error != null) {
            return ResponseEntity.badRequest().body("User denied permissions or an error occurred: " + error);
        }

        try {
            // Step 1: Exchange the code for a short-lived user access token
            String tokenExchangeUrl = String.format(
                "%s?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s",
                FB_TOKEN_URL, appId, redirectUri, appSecret, code
            );

            Map<String, Object> tokenResponse = restTemplate.getForObject(tokenExchangeUrl, Map.class);
            String userAccessToken = (String) tokenResponse.get("access_token");

            // Step 2: Retrieve all pages the user manages, including their Page Access Tokens
            String pagesUrl = FB_ME_ACCOUNTS_URL + "?access_token=" + userAccessToken;
            Map<String, Object> pagesResponse = restTemplate.getForObject(pagesUrl, Map.class);

            // Step 3: Return the pages and tokens (you can store them in your DB instead)
            return ResponseEntity.ok(pagesResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Facebook token exchange: " + e.getMessage());
        }
    }
    
}
