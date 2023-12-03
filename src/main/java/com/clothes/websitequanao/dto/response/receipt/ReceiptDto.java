package com.clothes.websitequanao.dto.response.receipt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDto {
    private String receiptCode;
    private String staffName;
    private LocalDateTime receiptDate;
    private LocalDateTime receivedDate;
    private BigDecimal totalPrice;
    private int status;
    private String supplierName;
    private String note;
    private List<ReceiptDetailDto> listDetail;
}
