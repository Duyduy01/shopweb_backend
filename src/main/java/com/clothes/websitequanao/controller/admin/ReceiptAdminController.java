package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.request.receipt.ReceiptRequestDto;
import com.clothes.websitequanao.dto.request.receipt.UpdaterReceiptRequest;
import com.clothes.websitequanao.dto.response.receipt.ReceiptDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.export.ExportReceiptPDF;
import com.clothes.websitequanao.service.ReceiptService;
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

@RestController
@RequestMapping("/api/v1/admin/receipt")
@Slf4j
@RequiredArgsConstructor
public class ReceiptAdminController {

    private final ReceiptService receiptService;


    @GetMapping
    public ResponseEntity<?> getAllReceipt() {

        ServiceResponse response = receiptService.getAll();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReceiptById(@PathVariable Long id) {

        ServiceResponse response = receiptService.getReceiptById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    // update product
    @GetMapping("/product")
    public ResponseEntity<?> getProduct() {

        ServiceResponse response = receiptService.getAllProduct();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    // nhập kho
    @PostMapping
    public ResponseEntity<?> addReceipt(@RequestBody ReceiptRequestDto dto) {

        ServiceResponse response = receiptService.addReceipt(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    // update nhà cung cấp
    // nhập kho
    @PostMapping("/user-manu")
    public ResponseEntity<?> updateUserManuReceipt(@RequestBody UpdaterReceiptRequest dto) {
        ServiceResponse response = receiptService.updateUserManu(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/status/{receiptId}")
    public ResponseEntity<?> updateStatusReceipt(@PathVariable Long receiptId) {
        ServiceResponse response = receiptService.updateStatusReceipt(receiptId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/cancel/{receiptId}")
    public ResponseEntity<?> cancelBill(@PathVariable Long receiptId) {
        ServiceResponse response = receiptService.cancelReceipt(receiptId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/export/pdf/{receiptId}")
    public void exportPdf(HttpServletResponse response, @PathVariable Long receiptId) throws IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        ReceiptDto result = (ReceiptDto) receiptService.getReceiptById(receiptId).getData();

        ExportReceiptPDF exportFileExcel = new ExportReceiptPDF(result);
        exportFileExcel.exportPDF(response);
    }

}
