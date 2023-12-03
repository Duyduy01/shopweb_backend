package com.clothes.websitequanao.dto.response.campaign;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminCampaignResponseDTO {
    private Long id;
    private List<String> productName;
    private Integer discount;
    private Integer status;
}
