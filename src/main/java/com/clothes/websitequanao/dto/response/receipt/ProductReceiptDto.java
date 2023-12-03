package com.clothes.websitequanao.dto.response.receipt;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductReceiptDto {

    private Long id;
    private boolean checkSelect;
    private String productName;
    private String productCode;
    private BigDecimal price;
    private BigDecimal priceSell;
//    private String description;
//    private String content;
//    private Long categoryId;
//    private Long brandId;
    private Long parentId;
    private String img;
//    private int active;
//    private String type;
    private int quantity;

    private Boolean checkChildren;

    private List<ProductReceiptDto> productChild;

}
