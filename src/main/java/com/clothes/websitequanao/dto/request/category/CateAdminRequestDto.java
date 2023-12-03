package com.clothes.websitequanao.dto.request.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CateAdminRequestDto {
    private Long id;
    private String categoryName;
    private String content;
    private Long parentId;
    private int status;
}
