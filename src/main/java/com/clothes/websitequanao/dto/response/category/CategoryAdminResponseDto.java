package com.clothes.websitequanao.dto.response.category;

import com.clothes.websitequanao.entity.CategoryEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryAdminResponseDto {

    private Long id;
    private String categoryName;
    private String categoryCode;
    private String content;
    private Long parentId;
    private List<CategoryEntity> listChild;
    private boolean boolActive;

}
