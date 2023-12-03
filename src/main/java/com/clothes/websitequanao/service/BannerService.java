package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.BannerAdminRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface BannerService {

    ServiceResponse getAllBannerByCode(String bannerCode);

    //admin
    ServiceResponse getAllBanner();

    ServiceResponse getBannerById(Long id);

    ServiceResponse editBanner(BannerAdminRequestDto dto);
    //admin
}
