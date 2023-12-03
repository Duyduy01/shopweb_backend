package com.clothes.websitequanao.export;

import com.clothes.websitequanao.export.report_dto.ReportSales;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.SHOP_NAME;

@Slf4j
public class ExportFileExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<ReportSales> reportSales;

    private String date;

    private String fromDate;
    private String toDate;

    public ExportFileExcel(List<ReportSales> reportSales, String date) {
        this.reportSales = reportSales;
        this.date = date;
        workbook = new XSSFWorkbook();
    }

    public ExportFileExcel(List<ReportSales> reportSales, String date, String fromDate, String toDate) {
        this.reportSales = reportSales;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.date = date;
        workbook = new XSSFWorkbook();
    }


    public void writeHeader() {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(24);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        // merge cell
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        // tạo tên cửa hàng
        Cell cell = row.createCell(0);
        cell.setCellValue(SHOP_NAME);

        cell.setCellStyle(style);
    }

    public void writeBody() {
        int rowCount = 3;
        Row row = sheet.createRow(rowCount);
        createCell(row, 0, "Số lượng sản phẩm bán: ");
        createCell(row, 1, reportSales.stream().map(e -> e.getQuantity())
                .reduce(0, Integer::sum));


        rowCount = 4;
        row = sheet.createRow(rowCount);
        createCell(row, 0, "Tổng doanh số theo ngày: ");
        createCell(row, 1, reportSales.stream().map(e -> e.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add));


    }

    public void writeTitleTable() {
        // title "báo cáo doanh số theo ngày
        Row row = sheet.createRow(8);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        // merge cell
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 6));
        // tạo tên cửa hàng
        Cell cell = row.createCell(0);
        if (!date.equals("SEVERAL")) {
            List<String> dateSplit = Arrays.stream(date.split("-")).collect(Collectors.toList());
            Collections.reverse(dateSplit);
            String dateExportRP = dateSplit.stream().collect(Collectors.joining("-"));
            cell.setCellValue("Báo cáo doanh số theo ngày: " + dateExportRP);
            cell.setCellStyle(style);
        } else {
            List<String> fromDateSplit = Arrays.stream(fromDate.split("-")).collect(Collectors.toList());
            Collections.reverse(fromDateSplit);
            String fromDate = fromDateSplit.stream().collect(Collectors.joining("-"));

            List<String> toDateSplit = Arrays.stream(toDate.split("-")).collect(Collectors.toList());
            Collections.reverse(toDateSplit);
            String toDate = toDateSplit.stream().collect(Collectors.joining("-"));

            cell.setCellValue("Báo cáo doanh số từ ngày: " + fromDate + " đến " + toDate);
            cell.setCellStyle(style);
        }

    }

    public void writeHeaderTable(List<String> headerLine) {
        Row row = sheet.createRow(9);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);


        int index = 0;
        for (String hl : headerLine) {
            sheet.autoSizeColumn(index);
            Cell cell = row.createCell(index);
            cell.setCellValue(hl);
            cell.setCellStyle(style);
            index++;
        }
    }

    public void writeValueTable() {
        int rowCount = 10;
        int index = 1;
        for (ReportSales rp : reportSales) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCellTable(row, columnCount++, index);
            createCellTable(row, columnCount++, rp.getCode());
            createCellTable(row, columnCount++, rp.getUserName());
            createCellTable(row, columnCount++, rp.getBillDate());
            createCellTable(row, columnCount++, rp.getDeliveryTime());
            createCellTable(row, columnCount++, rp.getQuantity());
            createCellTable(row, columnCount++, rp.getTotalPrice());
            index++;
        }
    }

    public void writeFooter() {

    }

    // tạo dữ liệu trong ô
    private void createCell(Row row, int columnCount, Object value) {
        CellStyle style = workbook.createCellStyle();
        createStyle(style);
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        setValueCell(value, cell, style);
    }

    // tạo dữ liệu trong cho table
    private void createCellTable(Row row, int columnCount, Object value) {
        CellStyle style = workbook.createCellStyle();
        createStyleTable(style);
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        setValueCell(value, cell, style);
    }

    // set value info cell
    private void setValueCell(Object value, Cell cell, CellStyle style) {
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(Date.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant()));
            DataFormat format = workbook.createDataFormat();
            style.setDataFormat(format.getFormat("m/d/yyyy h:mm;@"));
        } else if (value instanceof BigDecimal) {
//            cell.set
            cell.setCellValue(Double.valueOf(String.valueOf(value)));
            DataFormat format = workbook.createDataFormat();
            style.setDataFormat(format.getFormat("#,##0 \"VND\""));
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public void exportExcel(HttpServletResponse response) throws IOException {
        sheet = workbook.createSheet("Báo cáo doanh thu");
        sheet.setDisplayGridlines(false);
        /* Header */
        writeHeader();
        /* Body */
        writeBody();
        /* Body */

        /* Table */
        writeTitleTable();
        List<String> headerLine = Arrays.asList("STT", "Mã hóa đơn", "Tên khách hàng", "Ngày đặt ngày đặt", "Ngày đặt ngày nhận", "Số lượng", "Tổng");
        writeHeaderTable(headerLine);
        writeValueTable();
        /* Table */

        /* Footer */
        writeFooter();
        /* Footer */
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }


    private void createStyle(CellStyle style) {
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
    }

    private void createStyleTable(CellStyle style) {
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
    }
}
