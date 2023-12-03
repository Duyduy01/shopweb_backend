package com.clothes.websitequanao.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ReceiptAdminResponseDto {

    private Long id;
    private String code;
    private String userId;
    private LocalDateTime receiptDate;
    private LocalDateTime receivedDate;
    private BigDecimal totalPrice;
    private int status;
    private String supplierId;
    private String note;

    private boolean checkChooseManu;
    private Long supplierIdChange;
}
