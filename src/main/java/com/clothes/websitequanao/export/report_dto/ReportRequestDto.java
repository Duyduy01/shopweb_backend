package com.clothes.websitequanao.export.report_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    private String oneDay;


    //server-day
    private String fromDate;
    private String toDate;
}
