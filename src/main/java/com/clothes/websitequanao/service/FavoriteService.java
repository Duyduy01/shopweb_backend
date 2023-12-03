package com.clothes.websitequanao.service;

import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;

import java.util.List;

public interface FavoriteService {

    ServiceResponse favoriteProduct(Long productId);

    ServiceResponse getFavoriteProduct(Integer page, Integer limit);

    ServiceResponse totalFavoriteProduct();

    ServiceResponse quantityFavoriteProduct();

    // lisst
    void setFavorite(List<ProductEntity> productEntityList);

    // detail

    boolean getFavoriteProductDetail(Long productId);

}
