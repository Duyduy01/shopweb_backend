package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.BannerAdminRequestDto;
import com.clothes.websitequanao.dto.response.BannerAdminResponseDto;
import com.clothes.websitequanao.entity.BannerEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.BannerRepo;
import com.clothes.websitequanao.service.BannerService;
import com.clothes.websitequanao.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannerServiceImpl implements BannerService {

    private final BannerRepo bannerRepo;

    private final FileService fileService;

    @Override
    public ServiceResponse getAllBannerByCode(String bannerCode) {
        try {
            List<BannerEntity> results = bannerRepo.findAllByBannerCode(bannerCode);
            return ServiceResponse.RESPONSE_SUCCESS(results);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get all banner by code");
            return ServiceResponse.RESPONSE_ERROR("error get all banner by code");
        }

    }

    @Override
    public ServiceResponse getAllBanner() {
        try {
            List<BannerAdminResponseDto> result = new ArrayList<>();

            Set<String> bannerCode = bannerRepo.getBannerCode();

            for (String code : bannerCode) {
                List<BannerEntity> bannerEntities = bannerRepo.findAllByBannerCode(code);

                BannerAdminResponseDto response = BannerAdminResponseDto.builder()
                        .code(code)
                        .bannerEntities(bannerEntities)
                        .build();
                result.add(response);
            }

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get all admin banner");
            return ServiceResponse.RESPONSE_ERROR("error get all admin banner");
        }
    }

    @Override
    public ServiceResponse getBannerById(Long id) {
        try {
            BannerEntity result = bannerRepo.findById(id).orElseThrow(() -> new NullPointerException("Banner do not exist"));

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get all admin banner by id");
            return ServiceResponse.RESPONSE_ERROR("error get all admin banner by id");
        }
    }

    @Override
    public ServiceResponse editBanner(BannerAdminRequestDto dto) {
        try {
            BannerEntity result = bannerRepo.findById(dto.getId()).orElseThrow(() -> new NullPointerException("Banner do not exist"));
            if (dto.getImg() != null) {
                String img = (String) fileService.upload(dto.getImg());
                result.setImg(img);
            }
            result.setBannerName(dto.getBannerName());
            result.setBannerLink(dto.getBannerLink());

            bannerRepo.save(result);
            return ServiceResponse.RESPONSE_SUCCESS("Thay đổi banner thành công");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error edit  admin banner by id");
            return ServiceResponse.RESPONSE_ERROR("error edit  admin banner by id");
        }
    }
}
