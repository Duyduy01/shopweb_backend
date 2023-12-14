package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.product.ProductAdminRequestDto;
import com.clothes.websitequanao.dto.request.product.ProductDetailRequestDto;
import com.clothes.websitequanao.dto.request.product.ProductRequestDto;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {


    // home
    ServiceResponse getProductNew();

    ServiceResponse productOptionHome(Integer option);
    // home

    List<ProductEntity> getAllProduct(Long id);

    ServiceResponse addOrEditProduct(ProductAdminRequestDto dto);

    boolean checkExistChild(Long id);

    int getTotalQuantityOfChildren(Long parentId);

    ServiceResponse getOneProductById(Long id);

    ServiceResponse updateStatus(Long id);

    // filter
    ServiceResponse filterProduct(Map<String, List<String>> dto);

    ServiceResponse filterTotalPage(Map<String, List<String>> dto);

    // filter

    //search
    ServiceResponse searchProductByName(ProductRequestDto dto);
    ServiceResponse totalProductBySearch(ProductRequestDto dto);
    ServiceResponse totalPageProductBySearch(ProductRequestDto dto);
    //search

    //product detail
    ServiceResponse getProductDetailById(Long id);

    //click  color và size
    ServiceResponse getProductDetailBySpe(ProductDetailRequestDto dto);
    //product deteail

}
