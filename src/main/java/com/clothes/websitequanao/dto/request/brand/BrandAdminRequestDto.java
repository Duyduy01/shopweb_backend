package com.clothes.websitequanao.dto.request.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandAdminRequestDto {

    private Long id;
    private String brandName;
    private String content;
    private Integer status;
    private MultipartFile icon;
}
