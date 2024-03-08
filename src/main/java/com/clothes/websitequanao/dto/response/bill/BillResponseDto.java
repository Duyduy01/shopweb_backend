package com.clothes.websitequanao.dto.response.bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDto {
    private Long billId;
    private String billCode;
    private String userName;
    private LocalDateTime billDate;
    private LocalDateTime deliveryTime;
    private BigDecimal totalPrice;
    private BigDecimal shippingCost;
    private BigDecimal invoiceValue;

    private int status;
    private String staffName;
    private String note;
    private List<BillDetailDto> listDetail;


    private String fullName;
    private String email;
    private String phone;
    private String address;

    private Integer payment;

}
