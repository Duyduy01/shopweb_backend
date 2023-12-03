package com.clothes.websitequanao.export.report_dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportSales {

    private Long id;
    private String code;
    private String userName;
    private LocalDateTime billDate;
    private Integer quantity;
    private BigDecimal ship;
    private BigDecimal totalPrice;
    private LocalDateTime deliveryTime;
    private Integer status;
    private String staffName;
    private String note;
    private String addressDetail;
}
