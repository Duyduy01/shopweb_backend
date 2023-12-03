package com.clothes.websitequanao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "product_detail")
@NoArgsConstructor
public class ProductDetailEntity extends BaseEnity{

  private Long productId;
  private int count;
  private Long productBillId;
  private Long productReceiptId;
  private int status;
  private String code;

}
