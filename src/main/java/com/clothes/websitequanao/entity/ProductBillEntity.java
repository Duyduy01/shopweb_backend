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
@Table(name = "product_bill")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBillEntity extends BaseEnity{

  private long productId;
  private long billId;
  private BigDecimal priceSell;
  private int quantity;
  private BigDecimal totalPrice;
  private Integer status;
}
