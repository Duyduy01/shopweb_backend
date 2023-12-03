package com.clothes.websitequanao.dto.response;

import com.clothes.websitequanao.entity.BannerEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BannerAdminResponseDto {
    private String code;
    private List<BannerEntity> bannerEntities;
}
