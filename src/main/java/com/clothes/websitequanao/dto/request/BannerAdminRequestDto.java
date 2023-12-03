package com.clothes.websitequanao.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BannerAdminRequestDto {
    private Long id;

    private String bannerName;

    private MultipartFile img;

    private String bannerLink;
}
