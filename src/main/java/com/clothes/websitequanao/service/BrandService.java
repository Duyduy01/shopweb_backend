package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.BrandProductRequestDto;
import com.clothes.websitequanao.dto.request.brand.BrandAdminRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface BrandService {
    ServiceResponse findAll();

    //admin
    ServiceResponse getAllBrand();

    ServiceResponse addOrEditBrand(BrandAdminRequestDto dto);
//
    ServiceResponse deleteBrandById(Long id);

    //update status

    ServiceResponse updateStatus(Long id);

    ServiceResponse getProductByBrand(BrandProductRequestDto dto);


    ServiceResponse getTotalByBrand(BrandProductRequestDto dto);
}
