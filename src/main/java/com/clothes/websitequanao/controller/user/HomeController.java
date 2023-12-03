package com.clothes.websitequanao.controller.user;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class HomeController {
    private final ProductRepo productRepo;

    private final ProductService productService;

    @GetMapping("/product-home")
    public ResponseEntity<?> getAllProduct() {

        ServiceResponse result = productService.getProductNew();

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/product/option")
    public ResponseEntity<?> getProductByOption( @RequestParam(defaultValue = "1") Integer option) {

        ServiceResponse result = productService.productOptionHome(option);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
