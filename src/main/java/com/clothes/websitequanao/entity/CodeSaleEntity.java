package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "code_sale")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSaleEntity extends BaseEnity {
    private String saleCode;
    private Integer saleValue;
    private Integer saleNumber;
    private Integer status;
    private String listUserId;
    private String note;
}
