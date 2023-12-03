package com.clothes.websitequanao.dto.request.bill;

import lombok.Data;

@Data
public class BillUserRequestDTO {

    private String address;
    private String ward;
    private String district;
    private String city;
    private Integer pay;
    private String note;
    private String addressDetail;

    private Long productId;

    private Integer quantity;
}
