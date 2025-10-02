package com.confirmly.demo.Services;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class WebhookService {

    private final ApiCallService apiCallService;

    private String pageAccessToken = "EAARox8KuRy0BPDKjK4GI6oTzSnFLsJj5LO47xH5IOnJbqZCwA4phUsqirDwfPiPwWKHPazBb0TH67oYqVMPfITwLoJY0vS4kqqUxteAZCZB2wQEP5gtXjavJk4ZAW9nisxS2vnQxeJYwq46cncNsxoZCcsZBQW6kID1gWq6nOW07KUzb8fshxE3ZCdupJY0Tv6RtAc6G0h5";
    private final String MESSAGES_API = "https://graph.facebook.com/v19.0/me/messages";

    public WebhookService( ApiCallService apiCallService) {
        this.apiCallService = apiCallService;
    }

    public ResponseEntity<String> verifyWebhook(String mode, String challenge, String verifyToken) {
        if ("subscribe".equals(mode) && "helloworld".equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body("Verification failed");
    }

    public ResponseEntity<String> handleWebhook(String payload, String signature) {
        
        if (!isValidSignature(payload, signature)) {
            return ResponseEntity.status(401).body("Invalid signature");
        }

        JsonObject webhookEvent = JsonParser.parseString(payload).getAsJsonObject();
        if (isPageEvent(webhookEvent)) {
            processWebhookEntries(webhookEvent);
        }

        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    private boolean isPageEvent(JsonObject webhookEvent) {
        return webhookEvent.has("object") && 
               "page".equals(webhookEvent.get("object").getAsString());
    }

    private void processWebhookEntries(JsonObject webhookEvent) {
        JsonArray entries = webhookEvent.getAsJsonArray("entry");
        for (JsonElement entry : entries) {
            JsonObject entryObj = entry.getAsJsonObject();
            JsonArray messagingArray = entryObj.getAsJsonArray("messaging");
            processMessaging(messagingArray);
        }
    }

    private void processMessaging(JsonArray messagingArray) {
        for (JsonElement message : messagingArray) {
            JsonObject messagingObj = message.getAsJsonObject();
            String senderId = messagingObj.getAsJsonObject("sender").get("id").getAsString();

            if (messagingObj.has("message")) {
                handleMessage(messagingObj, senderId);
            } else if (messagingObj.has("postback")) {
                handlePostback(messagingObj);
            }
        }
    }

    private void handleMessage(JsonObject messagingObj, String senderId) {
        String text = messagingObj.getAsJsonObject("message").get("text").getAsString();
        String responseText = apiCallService.generateResponse(text);
        sendTextMessage(senderId, responseText);
    }

    private void handlePostback(JsonObject messagingObj) {
        String postbackPayload = messagingObj.getAsJsonObject("postback")
                .get("payload").getAsString();
        System.out.println("Received postback: " + postbackPayload);
    }

    public void sendTextMessage(String recipientId, String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        JsonObject recipient = new JsonObject();
        recipient.addProperty("id", recipientId);

        JsonObject messageContent = new JsonObject();
        messageContent.addProperty("text", text);

        JsonObject message = new JsonObject();
        message.add("recipient", recipient);
        message.add("message", messageContent);

        HttpEntity<String> request = new HttpEntity<>(message.toString(), headers);
        
        RestTemplate restTemplate = new RestTemplate();
        String url = MESSAGES_API + "?access_token=" + pageAccessToken;
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        System.out.println("Message sent. Response: " + response.getBody());
    }
    
    public boolean isValidSignature(String payload, String signature) {
        String expectedSignature = "sha1=" + calculateHMAC(payload, "2e87dd551c7b456dbd5d9db8a139297b");
        return expectedSignature.equals(signature);
    }

    public String calculateHMAC(String data, String key) {
        try {
            Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
            sha1_HMAC.init(secret_key);
            byte[] hash = sha1_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating HMAC", e);
        }
    }
}
