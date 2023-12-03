package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.response.bill.BillResponseDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.export.ExportBillPDF;
import com.clothes.websitequanao.service.BillService;
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
@RequestMapping("/api/v1/admin/bill")
@Slf4j
@RequiredArgsConstructor
public class BillAdminController {

    private final BillService billService;


    @GetMapping
    public ResponseEntity<?> getAllBill() {

        ServiceResponse response = billService.getAllAdmin();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBillById(@PathVariable Long id) {

        ServiceResponse response = billService.getBillDetailById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/verify/{billId}")
    public ResponseEntity<?> veriftylBill(@PathVariable Long billId) {
        ServiceResponse response = billService.verifyBill(billId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
    @PutMapping("/cancel/{billId}")
    public ResponseEntity<?> cancelBill(@PathVariable Long billId) {
        ServiceResponse response = billService.cancelBill(billId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/status/{billId}")
    public ResponseEntity<?> updateStatusReceipt(@PathVariable Long billId) {
        ServiceResponse response = billService.updateStatusBill(billId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/export/pdf/{billId}")
    public void exportPdf(HttpServletResponse response, @PathVariable Long billId) throws IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        BillResponseDto result = (BillResponseDto) billService.getBillDetailById(billId).getData();

        ExportBillPDF exportFileExcel = new ExportBillPDF(result);
        exportFileExcel.exportPDF(response);


    }
}
