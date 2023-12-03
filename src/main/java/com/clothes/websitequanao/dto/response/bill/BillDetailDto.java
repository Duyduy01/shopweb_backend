package com.clothes.websitequanao.dto.response.bill;

import com.clothes.websitequanao.entity.SpecialityEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BillDetailDto {
    private Long productId;
    private String productName;
    private String productCode;
    private String img;
    private BigDecimal priceSell;
    private List<SpecialityEntity> speciality;
    private int quantity;
    private BigDecimal totalPrice;
    private Integer review;
    private Long parentId;
}
