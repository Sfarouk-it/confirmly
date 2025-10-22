package com.confirmly.demo.DTO.facebookSTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookAuthUrlResponse {
    private String authUrl;
}
