package com.confirmly.demo.DTO;

import java.util.List;
import lombok.Data;

@Data
public class FacebookPagesResponse {
    private List<FacebookPageData> data;
}
