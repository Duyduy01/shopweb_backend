package com.clothes.websitequanao.dto.response.receipt;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReceiptDetailDto {
    private String productName;
    private String productCode;
    private BigDecimal priceEntry;
    private int quantity;
    private BigDecimal totalPrice;
}
