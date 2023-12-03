package com.clothes.websitequanao.dto.request.product;

import lombok.Data;

@Data
public class ProductDetailRequestDto {
    private Long productId;
    private Long colorId;
    private Long sizeId;
}
