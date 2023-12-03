package com.clothes.websitequanao.dto.response.product;

import com.clothes.websitequanao.entity.SpecialityEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductDetailSpeResponseDto {
    private Long id;
    private String productName;
    private String productCode;
    private BigDecimal priceSell;
    private BigDecimal priceEntry;
    private String description;
    private String content;
    private Long categoryId;
    private Long brandId;
    private Long parentId;
    private String img;

    private int quantity;
    private int sold;

    private int sale;

    // danh gia
    private int stars;
    private int comment;


    // color
    private SpecialityEntity color;

    // size
    private  SpecialityEntity size;

    // img child
    private List<String> listImg;
}
