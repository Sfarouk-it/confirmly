package com.confirmly.demo.DTO.facebookSTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookPageDto {
    private String pageId;
    private String pageName;
    private String category;
    private String[] tasks;
}
