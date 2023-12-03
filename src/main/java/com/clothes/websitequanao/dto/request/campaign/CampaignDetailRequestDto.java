package com.clothes.websitequanao.dto.request.campaign;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CampaignDetailRequestDto {
    @NotBlank
    private Long campaignId;
    @NotBlank
    private Integer discount;
    @NotBlank
    private List<Long> listProductSelect;
}
