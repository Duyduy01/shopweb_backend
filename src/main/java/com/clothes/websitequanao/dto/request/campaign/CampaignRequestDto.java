package com.clothes.websitequanao.dto.request.campaign;

import lombok.Data;

@Data
public class CampaignRequestDto {
    private String campaignName;
    private String content;
    private String startDay;
    private String endDay;
}
