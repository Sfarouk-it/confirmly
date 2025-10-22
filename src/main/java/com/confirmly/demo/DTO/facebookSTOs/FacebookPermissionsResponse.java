package com.confirmly.demo.DTO.facebookSTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookPermissionsResponse {
    private String facebookId;
    private String name;
    private String email;
    private String accessToken;
    private Long expiresIn;   
}
