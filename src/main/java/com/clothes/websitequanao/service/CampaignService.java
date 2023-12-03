package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.campaign.CampaignDetailRequestDto;
import com.clothes.websitequanao.dto.request.campaign.CampaignRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface CampaignService {

    // campaign
    ServiceResponse getAllCampaignAdmin();

    ServiceResponse addCampaign(CampaignRequestDto dto);
    // campaign


    // campaign detail
    ServiceResponse getCampaignDetailById(Long id);
    ServiceResponse addCampaignDetail(CampaignDetailRequestDto dto);
    ServiceResponse finishCampaign(Long campaignId);
    // campaign detail

    ServiceResponse campaignGetProduct();

    // worker
    boolean workerCampaign();

}
