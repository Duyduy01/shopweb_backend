package com.clothes.websitequanao.dto.request.receipt;

import lombok.Data;

import java.util.List;

@Data
public class ReceiptRequestDto {
    private Long supplierId;
    private String note;
    private List<ProductReceiptRequestDto> listProductSelect;
}
