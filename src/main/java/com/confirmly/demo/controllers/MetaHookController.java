package com.confirmly.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.confirmly.demo.Services.WebhookService;


@RestController
@RequestMapping("/metaHookController")
public class MetaHookController {

    private final WebhookService webhookService;

    public MetaHookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @GetMapping("/messenger")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {
        return webhookService.verifyWebhook(mode, challenge, verifyToken);
    }

    @PostMapping("/messenger")
    public ResponseEntity<String> handleMessagingWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Hub-Signature") String signature) {
        return webhookService.handleWebhook(payload, signature);
    }
}
