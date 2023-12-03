package com.clothes.websitequanao.facade;

import com.clothes.websitequanao.exception.ServiceResponse;

public interface ProductFacade {

    ServiceResponse getAllProduct(Long id);

    ServiceResponse getAllProductActive(Long id);
}
