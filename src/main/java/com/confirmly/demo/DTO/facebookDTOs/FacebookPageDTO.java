package com.confirmly.demo.DTO.facebookDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookPageDTO {
    private String pageId;
    private String name;
    private String category;
    
    @JsonProperty("access_token")
    private String accessToken;
    
    private String[] tasks;
}
