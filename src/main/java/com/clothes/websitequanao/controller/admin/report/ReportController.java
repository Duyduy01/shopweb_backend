package com.clothes.websitequanao.controller.admin.report;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.export.ExportFileExcel;
import com.clothes.websitequanao.export.ExportFilePDF;
import com.clothes.websitequanao.export.report_dto.ReportRequestDto;
import com.clothes.websitequanao.export.report_dto.ReportSales;
import com.clothes.websitequanao.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/report")
@Slf4j
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;


    // one-day
    @PostMapping("/one-day")
    public List<ReportSales> reportOneDay(@RequestBody ReportRequestDto dto) {

        List<ReportSales> results = reportService.reportOneDay(dto);
        return results;
    }

    @PostMapping("/total/one-day")
    public ResponseEntity<?> reportOneDayTotal(@RequestBody ReportRequestDto dto) {
        ServiceResponse result = reportService.totalOneDay(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    //export excel
    @GetMapping("/one-day/export/excel/{date}")
    public void reportOneDayExportExcel(HttpServletResponse response, @PathVariable String date) throws IOException {

        ReportRequestDto dto = ReportRequestDto.builder()
                .oneDay(date).build();
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<ReportSales> results = reportService.reportOneDay(dto);

        ExportFileExcel exportFileExcel = new ExportFileExcel(results, dto.getOneDay());
        exportFileExcel.exportExcel(response);
    }

    //export pdf
    @GetMapping("/one-day/export/pdf/{date}")
    public void reportOneDayExportPDF(HttpServletResponse response, @PathVariable String date) throws IOException {
        ReportRequestDto dto = ReportRequestDto.builder()
                .oneDay(date).build();
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<ReportSales> results = reportService.reportOneDay(dto);

        ExportFilePDF exportFileExcel = new ExportFilePDF(results, dto.getOneDay());
        exportFileExcel.exportPDF(response);
    }

    // one-day
    ////////////////////////////////////
    // several-day
    @PostMapping("/several-days")
    public List<ReportSales> reportSeveralDay(@RequestBody ReportRequestDto dto) {
        List<ReportSales> results = reportService.reportSeveralDays(dto);
        return results;
    }

    @PostMapping("/total/several-days")
    public ResponseEntity<?> reportSeveralDaysTotal(@RequestBody ReportRequestDto dto) {
        ServiceResponse result = reportService.totalSeveralDays(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    //export excel
    @GetMapping("/several-days/export/excel/{fromDate}/to/{toDate}")
    public void reportSeveralDaysExportExcel(HttpServletResponse response, @PathVariable String fromDate, @PathVariable String toDate) throws IOException {

        ReportRequestDto dto = ReportRequestDto.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<ReportSales> results = reportService.reportSeveralDays(dto);

        ExportFileExcel exportFileExcel = new ExportFileExcel(results, "SEVERAL", dto.getFromDate(), dto.getToDate());
        exportFileExcel.exportExcel(response);
    }

    //export pdf
    @GetMapping("/several-days/export/pdf/{fromDate}/to/{toDate}")
    public void reportSeveralDaysExportPDF(HttpServletResponse response, @PathVariable String fromDate, @PathVariable String toDate) throws IOException {
        ReportRequestDto dto = ReportRequestDto.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<ReportSales> results = reportService.reportSeveralDays(dto);

        ExportFilePDF exportFileExcel = new ExportFilePDF(results, "SEVERAL", dto.getFromDate(), dto.getToDate());
        exportFileExcel.exportPDF(response);
    }
    //several-day
}
