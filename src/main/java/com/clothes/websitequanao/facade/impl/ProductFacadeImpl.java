package com.clothes.websitequanao.facade.impl;

import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.facade.ProductFacade;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.clothes.websitequanao.common.Consts.UserType.USER_ACTIVE;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;

    private final ProductRepo productRepo;

    @Override
    public ServiceResponse getAllProduct(Long id) {

        List<ProductEntity> result = productService.getAllProduct(id);

        result.forEach(e -> {
            if (id == null) {
                boolean checkExist = productService.checkExistChild(e.getId());
                e.setCheckChildren(checkExist);

                // Nếu là sản phẩm cha, kiểm tra và cập nhật tổng quantity của sản phẩm con
                int totalQuantityOfChildren = productService.getTotalQuantityOfChildren(e.getId());
                e.setQuantity(totalQuantityOfChildren);
            }
            e.setBoolActive(e.getActive() == 1 ? true : false);
        });

        return ServiceResponse.RESPONSE_SUCCESS(result);

    }

    @Override
    public ServiceResponse getAllProductActive(Long id) {
        List<ProductEntity> result = productRepo.findAllByParentIdAndActive(id, USER_ACTIVE);

        result.forEach(e -> {
            if (id == null) {
                boolean checkExist = productService.checkExistChild(e.getId());
                e.setCheckChildren(checkExist);
            }
            e.setBoolActive(e.getActive() == 1 ? true : false);
        });

        return ServiceResponse.RESPONSE_SUCCESS(result);
    }
}
