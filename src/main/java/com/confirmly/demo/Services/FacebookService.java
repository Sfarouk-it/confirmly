package com.confirmly.demo.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.confirmly.demo.DTO.FacebookAuthResponse;
import com.confirmly.demo.DTO.FacebookAuthUrlResponse;
import com.confirmly.demo.DTO.FacebookMessageRequest;
import com.confirmly.demo.DTO.FacebookPageData;
import com.confirmly.demo.DTO.FacebookPageDto;
import com.confirmly.demo.DTO.FacebookPagesResponse;
import com.confirmly.demo.DTO.FacebookTokenResponse;
import com.confirmly.demo.DTO.FacebookUserInfo;
import com.confirmly.demo.Repositories.FacebookAccountRepository;
import com.confirmly.demo.Repositories.FacebookPageRepository;
import com.confirmly.demo.Repositories.UserRepository;
import com.confirmly.demo.model.FacebookAccount;
import com.confirmly.demo.model.FacebookPage;
import com.confirmly.demo.model.Seller;

@Service
public class FacebookService {
    @Value("${facebook.app.id}")
    private String appId;
    
    @Value("${facebook.app.secret}")
    private String appSecret;
    
    @Value("${facebook.redirect.uri}")
    private String authRedirectUri;
    @Value("${facebook.redirect.uri}")
    private String permissionsRedirectUri;
    
    private static final String FACEBOOK_GRAPH_API = "https://graph.facebook.com/v18.0";
    private static final String FACEBOOK_OAUTH_URL = "https://www.facebook.com/v18.0/dialog/oauth";
    
    @Autowired
    private FacebookAccountRepository facebookAccountRepository;
    
    @Autowired
    private FacebookPageRepository facebookPageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
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

    public String generatePermissionsUrl(Long userId) {
        return UriComponentsBuilder.fromHttpUrl(FACEBOOK_OAUTH_URL)
                .queryParam("client_id", appId)
                .queryParam("redirect_uri", permissionsRedirectUri)
                .queryParam("scope", "pages_show_list,pages_messaging,pages_read_engagement,pages_manage_metadata")
                .queryParam("state", userId.toString())
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }
    
    public FacebookAuthResponse handleCallback(String code, Long userId) {
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
        
        // Save or update Facebook user
        Seller user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        FacebookAccount facebookAccount = facebookAccountRepository.findByFacebookId(userInfo.getId())
                .orElse(new FacebookUser());
        
        facebookUser.setFacebookId(userInfo.getId());
        facebookUser.setName(userInfo.getName());
        facebookUser.setEmail(userInfo.getEmail());
        facebookUser.setAccessToken(tokenResponse.getAccessToken());
        facebookUser.setLongLivedToken(longLivedToken);
        facebookUser.setTokenExpiresAt(System.currentTimeMillis() + (tokenResponse.getExpiresIn() * 1000));
        facebookUser.setUser(user);
        
        facebookAccountRepository.save(facebookUser);
        
        // Fetch and save pages
        fetchAndSavePages(facebookUser, longLivedToken);
        
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
    
    private void fetchAndSavePages(FacebookAccount facebookUser, String accessToken) {
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
            for (FacebookPageData pageData : pagesResponse.getData()) {
                FacebookPage page = facebookPageRepository.findByPageId(pageData.getId())
                        .orElse(new FacebookPage());
                
                page.setPageId(pageData.getId());
                page.setPageName(pageData.getName());
                page.setPageAccessToken(pageData.getAccessToken());
                page.setCategory(pageData.getCategory());
                page.setTasks(pageData.getTasks());
                page.setFacebookUser(facebookUser);
                
                facebookPageRepository.save(page);
            }
        }
    }
    
    public List<FacebookPageDto> getUserPages(Long userId) {
        FacebookAccount facebookUser = facebookAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Facebook account not connected"));
        
        List<FacebookPage> pages = facebookPageRepository.findByFacebookUserId(facebookUser.getId());
        
        return pages.stream()
                .map(page -> new FacebookPageDto(
                    page.getPageId(),
                    page.getPageName(),
                    page.getCategory(),
                    page.getTasks()
                ))
                .collect(Collectors.toList());
    }
    
    public String sendMessage(Long userId, FacebookMessageRequest request) {
        FacebookAccount facebookUser = facebookAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Facebook account not connected"));
        
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
    
    public void disconnectFacebook(Long userId) {
        FacebookAccount facebookUser = facebookAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Facebook account not connected"));
        
        List<FacebookPage> pages = facebookPageRepository.findByFacebookUserId(facebookUser.getId());
        facebookPageRepository.deleteAll(pages);
        

        facebookAccountRepository.delete(facebookUser);
    }
}
