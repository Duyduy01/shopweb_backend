package com.clothes.websitequanao.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ChartEntity implements Comparable<ChartEntity> {
    private Integer month;
    private BigDecimal totalPrice;

    @Override
    public int compareTo(ChartEntity o) {
        return month.compareTo(o.month);
    }
}
