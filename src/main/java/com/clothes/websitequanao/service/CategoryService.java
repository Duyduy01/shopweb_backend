package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.category.CateAdminRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface CategoryService {
    // admin

    ServiceResponse findAllByChild();

    ServiceResponse addOrEditCate(CateAdminRequestDto dto);

    ServiceResponse updateStatus(Long id);

    // parent
    ServiceResponse getAllParentId();

    // user
    ServiceResponse findAllCate();

}
