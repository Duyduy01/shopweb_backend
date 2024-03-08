package com.clothes.websitequanao.dto.request.bill;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillUserRequestDTO {

    private BigDecimal shippingCost;
    private BigDecimal invoiceValue;

    private String address;
    private String ward;
    private String district;
    private String city;
    private Integer pay;
    private String note;
    private String addressDetail;

    private Long productId;

    private Integer quantity;
}
