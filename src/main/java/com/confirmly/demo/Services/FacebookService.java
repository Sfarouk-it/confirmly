package com.confirmly.demo.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.confirmly.demo.DTO.facebookDTOs.FacebookAuthResponse;
import com.confirmly.demo.DTO.facebookDTOs.FacebookPageDTO;
import com.confirmly.demo.DTO.facebookDTOs.FacebookPagesResponse;
import com.confirmly.demo.DTO.facebookDTOs.FacebookTokenResponse;
import com.confirmly.demo.DTO.facebookDTOs.FacebookUserInfo;
import com.confirmly.demo.model.Business;
import com.confirmly.demo.model.FacebookAccount;
import com.confirmly.demo.model.Seller;

@Service
public class FacebookService {
    
    private String appId = "1898179597398420";
    
    private String appSecret = "596eac2836d70665a64b86d31e17d68c";

    private static final String FACEBOOK_GRAPH_API = "https://graph.facebook.com/v18.0";
    private static final String FACEBOOK_OAUTH_URL = "https://www.facebook.com/v18.0/dialog/oauth";
    private String authRedirectUri = "https://confirmly.onrender.com/api/auth/facebook/redirect";
    private String permissionsRedirectUri = "https://confirmly.onrender.com/api/platformsAuth/facebook";
    
    @Autowired
    FacebookAccountsService fAccountsService;

    @Autowired
    private FacebookPagesService facebookPagesService;
   
    @Autowired
    private SellerService sellerService;

    private BusinessService businessService;
    
    private final WebClient webClient = WebClient.builder().build();
    
    public String generateAuthUrl() {
        return UriComponentsBuilder.fromHttpUrl(FACEBOOK_OAUTH_URL)
                .queryParam("client_id", appId)
                .queryParam("redirect_uri", authRedirectUri)
                .queryParam("scope", "email,public_profile")
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    public String generatePermissionsUrl(String businessID) {
        return UriComponentsBuilder.fromHttpUrl(FACEBOOK_OAUTH_URL)
                .queryParam("client_id", appId)
                .queryParam("redirect_uri", permissionsRedirectUri)
                .queryParam("scope", "pages_show_list,pages_messaging,pages_read_engagement,pages_manage_metadata")
                .queryParam("state", businessID)
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    public Seller handeleAuthCallback(String code){
        String tokenUrl = UriComponentsBuilder.fromHttpUrl(FACEBOOK_GRAPH_API + "/oauth/access_token")
                .queryParam("client_id", appId)
                .queryParam("client_secret", appSecret)
                .queryParam("redirect_uri", permissionsRedirectUri)
                .queryParam("code", code)
                .build()
                .toUriString();

        FacebookTokenResponse tokenResponse = webClient.get()
                .uri(tokenUrl)
                .retrieve()
                .bodyToMono(FacebookTokenResponse.class)
                .block();
                
        if (tokenResponse == null && tokenResponse.getAccessToken() == null) {
            throw new RuntimeException("Failed to get access token from Facebook");
        }

        FacebookUserInfo userInfo = getUserInfo(tokenResponse.getAccessToken());

        String Username = "fb_" + userInfo.getId();
        Seller seller;
        try {
                seller = sellerService.registerSellerbyFacebook(Username, userInfo.getEmail(), userInfo.getId());
        } catch (Exception e) {
                return null;
        }
        
        return seller;
    }


    
    public FacebookAuthResponse handlePermissionsCallback(String code, Long businessID) {
        // Exchange code for access token
        String tokenUrl = UriComponentsBuilder.fromHttpUrl(FACEBOOK_GRAPH_API + "/oauth/access_token")
                .queryParam("client_id", appId)
                .queryParam("client_secret", appSecret)
                .queryParam("redirect_uri", permissionsRedirectUri)
                .queryParam("code", code)
                .build()
                .toUriString();
        
        FacebookTokenResponse tokenResponse = webClient.get()
                .uri(tokenUrl)
                .retrieve()
                .bodyToMono(FacebookTokenResponse.class)
                .block();
        
        
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new RuntimeException("Failed to get access token from Facebook");
        }
        
        // Get long-lived token (60 days)
        String longLivedToken = exchangeForLongLivedToken(tokenResponse.getAccessToken());
        
        // Get user info
        FacebookUserInfo userInfo = getUserInfo(longLivedToken);
        
        Business business = businessService.getBusinessById(businessID);
        
        
        FacebookAccount facebookAccount = fAccountsService.getFacebookAccountByFacebookId(userInfo.getId()).get();
        
        if (facebookAccount == null) {

            facebookAccount = fAccountsService.savefacebookAccount(
                userInfo,
                tokenResponse.getAccessToken(),
                longLivedToken,
                tokenResponse.getExpiresIn(),
                business
            );
            
        } else {
            facebookAccount.setAccessToken(tokenResponse.getAccessToken());
            facebookAccount.setLongLivedToken(longLivedToken);
            facebookAccount.setTokenExpiresAt(System.currentTimeMillis() + (tokenResponse.getExpiresIn() * 1000));
        }
        
        // Fetch and save pages
        fetchAndSavePages(facebookAccount, longLivedToken);
        
        return new FacebookAuthResponse(
            userInfo.getId(),
            userInfo.getName(),
            userInfo.getEmail(),
            longLivedToken,
            tokenResponse.getExpiresIn()
        );
    }
    
    private String exchangeForLongLivedToken(String shortLivedToken) {
        String url = UriComponentsBuilder.fromHttpUrl(FACEBOOK_GRAPH_API + "/oauth/access_token")
                .queryParam("grant_type", "fb_exchange_token")
                .queryParam("client_id", appId)
                .queryParam("client_secret", appSecret)
                .queryParam("fb_exchange_token", shortLivedToken)
                .build()
                .toUriString();
        
        FacebookTokenResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FacebookTokenResponse.class)
                .block();
        
        return response != null ? response.getAccessToken() : shortLivedToken;
    }
    
    private FacebookUserInfo getUserInfo(String accessToken) {
        String url = UriComponentsBuilder.fromHttpUrl(FACEBOOK_GRAPH_API + "/me")
                .queryParam("fields", "id,name,email")
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();
        
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FacebookUserInfo.class)
                .block();
    }
    
    private void fetchAndSavePages(FacebookAccount facebookAccount, String accessToken) {
        String url = UriComponentsBuilder.fromHttpUrl(FACEBOOK_GRAPH_API + "/me/accounts")
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();
        
        FacebookPagesResponse pagesResponse = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FacebookPagesResponse.class)
                .block();
        
        if (pagesResponse != null && pagesResponse.getData() != null) {
            for (FacebookPageDTO pageData : pagesResponse.getData()) {
                facebookPagesService.saveFacebookPage(pageData, facebookAccount);
            }
        }
    }
    
    /*
    public String sendMessage(Long userId, FacebookMessageRequest request) {
        FacebookAccount facebookUser = FacebookAccountsService
                .orElseThrow(() -> new RuntimeException("Facebook account not found for user"));
        
        FacebookPage page = facebookPageRepository.findByPageId(request.getPageId())
                .orElseThrow(() -> new RuntimeException("Page not found"));
        
        String url = FACEBOOK_GRAPH_API + "/me/messages";
        
        String requestBody = String.format(
            "{\"recipient\":{\"id\":\"%s\"},\"message\":{\"text\":\"%s\"}}",
            request.getRecipientId(),
            request.getMessage()
        );
        
        String response = webClient.post()
                .uri(url + "?access_token=" + page.getPageAccessToken())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        
        return response;
    }
         */
}
