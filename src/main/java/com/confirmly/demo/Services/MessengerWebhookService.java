package com.confirmly.demo.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class MessengerWebhookService {

    private final MessengerService messengerService;
    private final ApiCallService apiCallService;

    public MessengerWebhookService(MessengerService messengerService, ApiCallService apiCallService) {
        this.messengerService = messengerService;
        this.apiCallService = apiCallService;
    }

    public ResponseEntity<String> verifyWebhook(String mode, String challenge, String verifyToken) {
        if ("subscribe".equals(mode) && "helloworld".equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body("Verification failed");
    }

    public ResponseEntity<String> handleWebhook(String payload, String signature) {
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
        messengerService.sendTextMessage(senderId, responseText);
    }

    private void handlePostback(JsonObject messagingObj) {
        String postbackPayload = messagingObj.getAsJsonObject("postback")
                .get("payload").getAsString();
        System.out.println("Received postback: " + postbackPayload);
    }
}
