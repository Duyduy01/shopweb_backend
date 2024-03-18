package com.clothes.websitequanao.dto.response.bill;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BillAdminResponseDto {

    private Long id;
    private String code;
    private String userId;
    private LocalDateTime billDate;
    private LocalDateTime deliveryTime;
    private BigDecimal ship;
    private BigDecimal totalPrice;
    private BigDecimal invoiceValue;
    private int status;
    private String staffId;
    private String note;
    private int payment;

    private boolean checkBill;

}
