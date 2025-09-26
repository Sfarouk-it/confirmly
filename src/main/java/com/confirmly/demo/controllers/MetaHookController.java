package com.confirmly.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.confirmly.demo.Services.ApiCallService;
import com.confirmly.demo.Services.MessengerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/metaHookController")
public class MetaHookController {

    @Autowired
    MessengerService messengerService;

    @Autowired
    ApiCallService apiCallService;

    @GetMapping("/messenger")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {
        
        System.out.println(mode + " " + challenge + " " + verifyToken);
        
        
        if ("subscribe".equals(mode) && "helloworld".equals(verifyToken)) {
            return ResponseEntity.ok(challenge); 
        }
        return ResponseEntity.status(403).body("Verification failed");
    }

    @PostMapping("/messenger")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Hub-Signature") String signature) {
        
        if (!messengerService.isValidSignature(payload, signature)) {
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
            webhookEvent.get("object").getAsString().equals("page");
    }

    private void processWebhookEntries(JsonObject webhookEvent) {
        JsonArray entries = webhookEvent.getAsJsonArray("entry");
        for (JsonElement entry : entries) {
            JsonObject entryObj = entry.getAsJsonObject();
            processMessaging(entryObj.getAsJsonArray("messaging"));
        }
    }

    private void processMessaging(JsonArray messaging) {
        for (JsonElement message : messaging) {
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
        System.out.println("Received message: " + text + " from " + senderId);
        
        String responseText = apiCallService.generateResponse(text);
        messengerService.sendTextMessage(senderId, responseText);
    }

    private void handlePostback(JsonObject messagingObj) {
        String postbackPayload = messagingObj.getAsJsonObject("postback")
                .get("payload").getAsString();
        System.out.println("Received postback: " + postbackPayload);
    }
}