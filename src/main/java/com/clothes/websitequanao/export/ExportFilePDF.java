package com.clothes.websitequanao.export;

import com.clothes.websitequanao.export.report_dto.ReportSales;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.SHOP_NAME;

@Slf4j
public class ExportFilePDF {
    private List<ReportSales> reportSales;
    private String date;
    private String fromDate;
    private String toDate;

    public ExportFilePDF(List<ReportSales> reportSales, String date) {
        this.reportSales = reportSales;
        this.date = date;
    }

    public ExportFilePDF(List<ReportSales> reportSales, String date, String fromDate, String toDate) {
        this.reportSales = reportSales;
        this.date = date;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }


    public void writeHeader(Document document) {
        Font font = FontFactory.getFont("utf-8");
        font.setSize(16);
        Paragraph p = new Paragraph(SHOP_NAME, font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
    }


    public void writeBody(Document document, PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);

        Font font = FontFactory.getFont("utf-8");
        font.setSize(10);

        // quantity

        cell.setPhrase(new Phrase("Số lượng sản phẩm bán: ", font));
        table.addCell(cell);
        String quantity = String.valueOf(reportSales.stream().map(e -> e.getQuantity())
                .reduce(0, Integer::sum));
        cell.setPhrase(new Phrase(quantity, font));
        table.addCell(cell);
        // quantity

        // quantity
        cell.setPhrase(new Phrase("Tổng doanh số theo ngày:  ", font));
        table.addCell(cell);

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        vn.setMaximumFractionDigits(0);
        vn.setMinimumFractionDigits(0);

        String totalPrice = vn.format(reportSales.stream().map(e -> e.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)) + " VND";

        cell.setPhrase(new Phrase(totalPrice, font));
        table.addCell(cell);
        // quantity

        document.add(table);

    }


    public void writeTitleTable(Document document) {
        Font font = FontFactory.getFont("utf-8");
        font.setSize(12);
        if (!date.equals("SEVERAL")) {
            List<String> dateSplit = Arrays.stream(date.split("-")).collect(Collectors.toList());
            Collections.reverse(dateSplit);
            String dateExportRP = dateSplit.stream().collect(Collectors.joining("-"));
            Paragraph p = new Paragraph("Báo cáo doanh số ngày: " + dateExportRP, font);
            p.setSpacingBefore(20);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p);
        } else {
            List<String> fromDateSplit = Arrays.stream(fromDate.split("-")).collect(Collectors.toList());
            Collections.reverse(fromDateSplit);
            String fromDate = fromDateSplit.stream().collect(Collectors.joining("-"));

            List<String> toDateSplit = Arrays.stream(toDate.split("-")).collect(Collectors.toList());
            Collections.reverse(toDateSplit);
            String toDate = toDateSplit.stream().collect(Collectors.joining("-"));

            Paragraph p = new Paragraph("Báo cáo doanh số từ ngày: " + fromDate + " đến " + toDate, font);
            p.setSpacingBefore(20);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p);
        }

    }


    public void writeHeaderTable(List<String> headerLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont("utf-8");
        font.setSize(8);
        font.setColor(Color.WHITE);

        for (String hl : headerLine) {
            cell.setPhrase(new Phrase(hl, font));
            table.addCell(cell);
        }
    }


    public void writeValueTable(PdfPTable table, Document document) {
        Font font = FontFactory.getFont("utf-8");
        font.setSize(8);
        int index = 1;

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        vn.setMaximumFractionDigits(0);
        vn.setMinimumFractionDigits(0);


        for (ReportSales rp : reportSales) {

            table.addCell(createCell(String.valueOf(index), font));
            table.addCell(createCell(rp.getCode(), font));
            table.addCell(createCell(rp.getUserName(), font));
            table.addCell(createCell(rp.getBillDate().toString(), font));
            table.addCell(createCell(rp.getDeliveryTime().toString(), font));
            table.addCell(createCell(rp.getQuantity().toString(), font));
            table.addCell(createCell(vn.format(rp.getTotalPrice()) + " VND", font));
            index++;
        }
        document.add(table);
    }

    private PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }


    public void writeFooter() {

    }

    public void exportPDF(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        writeHeader(document);

        // table body
        PdfPTable tableBody = new PdfPTable(2);
        tableBody.setWidthPercentage(50f);
        tableBody.setHorizontalAlignment(0);
        tableBody.setSpacingBefore(10);
        writeBody(document, tableBody);
        // table body

        writeTitleTable(document);
        // set up table
        PdfPTable tableValue = new PdfPTable(7);
        tableValue.setWidthPercentage(100f);
        tableValue.setWidths(new float[]{1f, 3.0f, 4.0f, 4.5f, 4.5f, 1f, 4.0f});
        tableValue.setSpacingBefore(10);
        // title table

        List<String> headerLine = Arrays.asList("#", "Mã hóa đơn", "khách hàng", "Ngày đặt", "Ngày nhận", "SL", "Tổng");
        writeHeaderTable(headerLine, tableValue);
        // value
        writeValueTable(tableValue, document);

        document.close();
    }


}
