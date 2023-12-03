package com.clothes.websitequanao.dto.request;

import lombok.Data;

@Data
public class BrandProductRequestDto {
    private Long brand;
    private Long category;
    private Integer sort;
    private Integer page;
    private Integer limit;
}
