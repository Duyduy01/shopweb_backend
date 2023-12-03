package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.speciality.SpeNewRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface SpecialityService {

    ServiceResponse getSpeciality();

    ServiceResponse getName();

    ServiceResponse getSpecialityById(Long productId);

    // admin
    ServiceResponse getAllSpe();

    ServiceResponse getParentSpecialityById(Long productId);

    ServiceResponse getAllChildSpecialityByParentId(Long parentId);

    ServiceResponse addSpeNew(SpeNewRequestDto dto);

    ServiceResponse addSpeOld(SpeNewRequestDto dto);

    ServiceResponse editSpeOld(SpeNewRequestDto dto);

    ServiceResponse updateStatus(Long id);
}
