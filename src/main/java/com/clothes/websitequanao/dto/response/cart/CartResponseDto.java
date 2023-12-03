package com.clothes.websitequanao.dto.response.cart;

import com.clothes.websitequanao.entity.SpecialityEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponseDto {
    private Long id;
    private Long parentId;
    private Long billId;
    private String productName;
    private String img;
    private Integer quantity;
    private BigDecimal priceSell;
    private Integer totalQuantity;
    private BigDecimal totalPrice;

    private String parentName;
    private List<SpecialityEntity> speciality;
}
