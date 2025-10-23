package com.confirmly.demo.DTO.facebookDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookUserInfo {
    private String id;
    private String name;
    private String email;
}
