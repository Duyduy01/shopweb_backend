package com.clothes.websitequanao.dto.request.cart;

import lombok.Data;

import java.util.List;

@Data
public class CartRequestNoLoginDTO {
    List<CartRequestDTO> listProduct;
}
