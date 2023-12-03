package com.clothes.websitequanao.dto.request.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartRequestDTO {
    private Long productId;
    private int quantity;
    private Long billId;
}
