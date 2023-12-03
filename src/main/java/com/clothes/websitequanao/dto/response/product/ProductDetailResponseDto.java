package com.clothes.websitequanao.dto.response.product;

import com.clothes.websitequanao.entity.SpecialityEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
public class ProductDetailResponseDto {
    private Long id;
    private String productName;
    private String productCode;
    private BigDecimal priceSell;
    private BigDecimal priceEntry;
    private Integer sale;
    private String description;
    private String content;
    private Long categoryId;
    private Long brandId;

    private String brandName;
    private Long parentId;
    private String img;

    private int quantity;
    private int sold;

    // danh gia
    private float rate;
    private int totalRate;
    private int totalPay;


    private boolean favorite;

    // color
    private List<SpecialityEntity> color;

    // size
    private List<SpecialityEntity> size;

    // img child
    private List<String> listImg;
}
