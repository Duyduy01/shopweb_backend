package com.clothes.websitequanao.dto.request.speciality;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpeNewRequestDto {
    private Long id;

    private String featuredName;
    private String description;
    private String featuredKey;
    private Integer active;

    private String featuredCode;
}
