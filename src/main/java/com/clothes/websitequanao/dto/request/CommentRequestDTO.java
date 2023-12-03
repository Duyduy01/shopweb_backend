package com.clothes.websitequanao.dto.request;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Long billId;
    private Long productId;
    private String title;
    private String content;
    private Integer stars;
}
