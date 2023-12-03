package com.clothes.websitequanao.controller.user;


import com.clothes.websitequanao.dto.request.bill.BillUserRequestDTO;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.BillService;
import com.clothes.websitequanao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v3/user")
public class BillController {
    private final BillService billService;
    private final UserService userService;

    @GetMapping("/detail")

    public ResponseEntity<?> getUserDetail() {
        ServiceResponse result = userService.getUserDetail();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/bill")
    public ResponseEntity<?> bill(@RequestBody BillUserRequestDTO dto) {
        ServiceResponse result = billService.bill(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    // user quản lý bill
    // get all Bill by status
    @GetMapping("/bill/status/{status}")
    public ResponseEntity<?> getAllBillByStatus(@PathVariable Integer status,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "4") Integer limit) {
        ServiceResponse result = billService.getAllByStatus(status, page, limit);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/bill/total/{status}")
    public ResponseEntity<?> totalByStatus(@PathVariable Integer status) {
        ServiceResponse result = billService.totalByStatus(status);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/bill/cancel/{billId}")
    public ResponseEntity<?> cancelBill(@PathVariable Long billId) {
        ServiceResponse response = billService.cancelBill(billId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/bill/detail/{billId}")
    public ResponseEntity<?> seeDetailBill(@PathVariable Long billId) {
        ServiceResponse result = billService.getBillDetailByIdUser(billId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    // user quản lý bill

    // bill pay now
    @GetMapping("/bill/pay-now")
    public ResponseEntity<?> getBillPayNow(@RequestParam Long productId, @RequestParam Integer quantity) {
        ServiceResponse result = billService.getBillPayNow(productId, quantity);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/bill/pay-now")
    public ResponseEntity<?> addBillPayNow(@RequestBody BillUserRequestDTO dto) {
        ServiceResponse result = billService.addBillPayNow(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    // bill pay now
}
