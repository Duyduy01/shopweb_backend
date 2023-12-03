package com.clothes.websitequanao.export;

import com.clothes.websitequanao.dto.response.bill.BillDetailDto;
import com.clothes.websitequanao.dto.response.bill.BillResponseDto;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.clothes.websitequanao.common.Consts.SHOP_NAME;

public class ExportBillPDF {
    private BillResponseDto dto;

    public ExportBillPDF(BillResponseDto dto) {
        this.dto = dto;
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

        Font fontTitle = FontFactory.getFont("utf-8", 10);


        Font font = FontFactory.getFont("utf-8", 10);
        font.setColor(Color.black);


        // mã hóa đơn
        cell.setPhrase(new Phrase("Mã hóa đơn: ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(dto.getBillCode(), font));
        table.addCell(cell);
        //  mã hóa đơn

        // thông tin khác hàng
        cell.setPhrase(new Phrase("Tên khách hàng: ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(dto.getFullName(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email: ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(dto.getEmail(), font));
        table.addCell(cell);


        cell.setPhrase(new Phrase("Số điện thoại: ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(dto.getPhone(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Địa chỉ nhận hàng: ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(dto.getAddress(), font));
        table.addCell(cell);
        // tên nhân viên


        // ngày đặt
        cell.setPhrase(new Phrase("Ngày đặt: ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(dto.getBillDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), font));
        table.addCell(cell);
        // ngày đặt


        // ngày nhập
        cell.setPhrase(new Phrase("Ngày nhập: ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(dto.getDeliveryTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), font));
        table.addCell(cell);
        // ngày nhập


        // Nhà cung cấp
        cell.setPhrase(new Phrase("Tên nhân viên : ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(String.valueOf(dto.getStaffName()), font));
        table.addCell(cell);
        // Nhà cung cấp

        // Nhà cung cấp
        cell.setPhrase(new Phrase("Ghi chú : ", fontTitle));
        table.addCell(cell);
        cell.setPhrase(new Phrase(String.valueOf(dto.getNote() == null ? "" : dto.getNote()), font));
        table.addCell(cell);
        // Nhà cung cấp


        document.add(table);

    }


    public void writeTitleTable(Document document) {
        Font font = FontFactory.getFont("utf-8");
        font.setSize(12);

        Paragraph p = new Paragraph("Thông tin sản phẩm", font);
        p.setSpacingBefore(10);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);


    }


    public void writeHeaderTable(java.util.List<String> headerLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLACK);
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

        for (BillDetailDto rp : dto.getListDetail()) {

            table.addCell(createCell(String.valueOf(index), font));
            table.addCell(createCell(rp.getProductName(), font));
            table.addCell(createCell(rp.getProductCode(), font));
            table.addCell(createCell(vn.format(rp.getPriceSell()) + " VND", font));
            table.addCell(createCell(String.valueOf(rp.getQuantity()), font));
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


    public void writeFooter(Document document, PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);

        Font font = FontFactory.getFont("utf-8");
        font.setSize(10);

        // mã hóa đơn
        cell.setPhrase(new Phrase("", font));
        table.addCell(cell);
        //  mã hóa đơn

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);

        // Tổng
        cell.setPhrase(new Phrase("Tổng: " + vn.format(dto.getTotalPrice()) + " VND", font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        // Tổng
        
        document.add(table);
    }

    public void exportPDF(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        writeHeader(document);

        // table body
        PdfPTable tableBody = new PdfPTable(2);
        tableBody.setWidthPercentage(100f);
        tableBody.setWidths(new float[]{1f, 3.0f});
        tableBody.setHorizontalAlignment(0);
        tableBody.setSpacingBefore(10);
        writeBody(document, tableBody);
        // table body

        writeTitleTable(document);
        // set up table
        PdfPTable tableValue = new PdfPTable(6);
        tableValue.setWidthPercentage(100f);
        tableValue.setWidths(new float[]{1f, 6.0f, 3.0f, 2f, 1f, 2.0f});
        tableValue.setSpacingBefore(10);
        // title table

        List<String> headerLine = Arrays.asList("#", "Tên", "Mã", "Giá", "SL", "Tổng");
        writeHeaderTable(headerLine, tableValue);
        // value
        writeValueTable(tableValue, document);


        // table footer
        PdfPTable tableFooter = new PdfPTable(2);
        tableFooter.setWidthPercentage(100f);
        tableFooter.setHorizontalAlignment(0);
        tableFooter.setSpacingBefore(10);
        writeFooter(document, tableFooter);
        // table footer


        document.close();
    }
}
