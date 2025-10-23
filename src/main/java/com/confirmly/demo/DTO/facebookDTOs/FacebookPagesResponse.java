package com.confirmly.demo.DTO.facebookDTOs;

import java.util.List;

import lombok.Data;

@Data
public class FacebookPagesResponse {
    private List<FacebookPageDTO> data;
}
