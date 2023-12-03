package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product_receipt")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReceiptEntity extends BaseEnity{

  private Long id;
  private Long productId;
  private Long receiptId;
  private Integer quantity;
  private BigDecimal priceEntry;
  private BigDecimal totalPrice;
}
