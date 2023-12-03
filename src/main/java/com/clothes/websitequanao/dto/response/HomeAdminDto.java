package com.clothes.websitequanao.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeAdminDto {
    public int billCount;
    public float ratio;
    public int quantity;
    public int userCount;
}
