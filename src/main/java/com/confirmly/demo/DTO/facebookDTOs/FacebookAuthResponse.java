package com.confirmly.demo.DTO.facebookDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookAuthResponse {
    private String facebookId;
    private String name;
    private String email;
    private String accessToken;
    private Long expiresIn;
}
