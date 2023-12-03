package com.clothes.websitequanao.controller.admin;


import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.service.ProductAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
@RequiredArgsConstructor
public class HomeAdminController {

    private final ProductRepo productRepo;

    private final ProductAdminService productAdminService;

    @GetMapping
    public List<ProductEntity> getProduct(HttpServletRequest request) {

        System.out.println(request.getHeader("Authorization"));
        System.out.println("user" + SecurityContextHolder.getContext().getAuthentication());
        return productRepo.findAll();
    }

    @GetMapping("/home")
    public ResponseEntity<?> homeAdmin() {
        ServiceResponse response = productAdminService.getInfoAdmin();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/chart/bill")
    public ResponseEntity<?> chartBill() {
        ServiceResponse result = productAdminService.getChartBill();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/chart/receipt")
    public ResponseEntity<?> chartReceipt() {
        ServiceResponse result = productAdminService.getChartReceipt();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


}
