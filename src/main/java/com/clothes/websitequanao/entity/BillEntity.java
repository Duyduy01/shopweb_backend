package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bill")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillEntity extends BaseEnity {

    private String code;
    private Long userId;
    private LocalDateTime billDate;
//    private BigDecimal ship;
    private BigDecimal totalPrice;
    private LocalDateTime deliveryTime;
    private Integer status;
    private Long staffId;
    private String note;
    private Integer payment;

    private String address;
}
