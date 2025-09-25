package com.confirmly.demo.Services;


import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiCallService {
    
    private String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private String apiKey = "AIzaSyBs4K4I5J4XMVPRmjz2Yph6GAXpEQA55xw";



    List<Map<String, String>> products = List.of(
        Map.of("name", "Classic White T-Shirt", "price", "1500 DZD", "sizes", "S, M, L, XL"),
        Map.of("name", "Black Logo T-Shirt", "price", "1800 DZD", "sizes", "M, L"),
        Map.of("name", "Blue V-Neck T-Shirt", "price", "1700 DZD", "sizes", "S, M, L"),
        Map.of("name", "Red Graphic Tee", "price", "2000 DZD", "sizes", "S, M, L, XL"),
        Map.of("name", "Green Polo Shirt", "price", "2500 DZD", "sizes", "M, L, XL"),
        Map.of("name", "Grey Oversized T-Shirt", "price", "2200 DZD", "sizes", "L, XL"),
        Map.of("name", "Yellow Summer Tee", "price", "1600 DZD", "sizes", "S, M"),
        Map.of("name", "Purple Slim Fit T-Shirt", "price", "2100 DZD", "sizes", "M, L"),
        Map.of("name", "Orange Casual T-Shirt", "price", "1500 DZD", "sizes", "S, M, L"),
        Map.of("name", "Brown Cotton Tee", "price", "1900 DZD", "sizes", "M, L"),
        Map.of("name", "White V-Neck T-Shirt", "price", "1600 DZD", "sizes", "S, M, L"),
        Map.of("name", "Black Round Neck T-Shirt", "price", "1750 DZD", "sizes", "S, M, L, XL"),
        Map.of("name", "Blue Striped Tee", "price", "1850 DZD", "sizes", "M, L"),
        Map.of("name", "Khaki Casual T-Shirt", "price", "1700 DZD", "sizes", "S, M"),
        Map.of("name", "Navy Logo Tee", "price", "1950 DZD", "sizes", "M, L, XL"),
        Map.of("name", "Pink Printed T-Shirt", "price", "2000 DZD", "sizes", "S, M"),
        Map.of("name", "White Oversized Tee", "price", "2300 DZD", "sizes", "L, XL"),
        Map.of("name", "Grey Polo Shirt", "price", "2400 DZD", "sizes", "M, L"),
        Map.of("name", "Sky Blue T-Shirt", "price", "1700 DZD", "sizes", "S, M, L"),
        Map.of("name", "Beige Classic Tee", "price", "1800 DZD", "sizes", "M, L"),
        Map.of("name", "Maroon Casual Tee", "price", "1750 DZD", "sizes", "S, M"),
        Map.of("name", "Dark Green T-Shirt", "price", "1850 DZD", "sizes", "M, L, XL"),
        Map.of("name", "Light Grey T-Shirt", "price", "1650 DZD", "sizes", "S, M, L"),
        Map.of("name", "Black Printed Tee", "price", "2100 DZD", "sizes", "M, L"),
        Map.of("name", "White Slim Fit T-Shirt", "price", "2200 DZD", "sizes", "S, M, L"),
        Map.of("name", "Red Polo Shirt", "price", "2500 DZD", "sizes", "M, L, XL"),
        Map.of("name", "Blue Graphic Tee", "price", "2000 DZD", "sizes", "S, M, L"),
        Map.of("name", "Yellow Logo T-Shirt", "price", "1800 DZD", "sizes", "S, M"),
        Map.of("name", "Olive Green T-Shirt", "price", "1900 DZD", "sizes", "M, L, XL"),
        Map.of("name", "Charcoal Grey T-Shirt", "price", "1700 DZD", "sizes", "S, M, L, XL")
    );



    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-goog-api-key", apiKey);
        return headers;
    }

    private Map<String, Object> buildContext(List<Map<String, String>> products) {
        
        StringBuilder productList = new StringBuilder("You are an assistant for Farouk T-Shirts. " +
            "Only answer based on the following product catalog:\n\n");

        for (Map<String, String> product : products) {
            productList.append("- ")
                    .append(product.get("name"))
                    .append(" | Price: ").append(product.get("price"))
                    .append(" | Sizes: ").append(product.get("sizes"))
                    .append("\n");
        }

        Map<String, Object> part = Map.of("text", productList.toString());
        return Map.of(
            "role", "user",
            "parts", List.of(part)
        );
    }

    private Map<String, Object> buildUserMessage(String message) {
        Map<String, Object> part = Map.of("text", message);
        return Map.of(
            "role", "user",
            "parts", List.of(part)
        );
    }

    private String sendRequest(Map<String, Object> requestBody) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, buildHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return extractMessage(response.getBody());
    }

    public String generateResponse(String requestMessage) {
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                buildContext(products),
                buildUserMessage(requestMessage)
            )
        );

        return sendRequest(requestBody);
    }

    public String extractMessage(String responseBody) {
    try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            return root.path("candidates")
                       .get(0)
                       .path("content")
                       .path("parts")
                       .get(0)
                       .path("text")
                       .asText()
                       .trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
}
}
