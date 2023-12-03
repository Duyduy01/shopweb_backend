package com.clothes.websitequanao.service;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.export.report_dto.ReportRequestDto;
import com.clothes.websitequanao.export.report_dto.ReportSales;

import java.util.List;

public interface ReportService {
    //one
    List<ReportSales> reportOneDay(ReportRequestDto dto);

    ServiceResponse totalOneDay(ReportRequestDto dto);
    //one

    //several
    List<ReportSales> reportSeveralDays(ReportRequestDto dto);
    ServiceResponse totalSeveralDays(ReportRequestDto dto);
    //serveral
}
