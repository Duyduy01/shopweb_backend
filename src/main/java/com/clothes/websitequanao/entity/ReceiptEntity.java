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
@Table(name = "receipt")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptEntity extends BaseEnity{

  private String code;
  private long userId;
  private LocalDateTime receiptDate;
  private LocalDateTime receivedDate;
  private BigDecimal totalPrice;
  private int status;
  private Long supplierId;
  private String note;

}
