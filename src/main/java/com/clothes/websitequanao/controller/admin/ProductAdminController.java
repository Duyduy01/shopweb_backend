package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.request.product.ProductAdminRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.facade.ProductFacade;
import com.clothes.websitequanao.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/product")
@Slf4j
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductFacade productFacade;

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProduct(@RequestParam(required = false) Long id) {

        ServiceResponse response = productFacade.getAllProduct(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/parent")
    public ResponseEntity<?> getProductByActive(@RequestParam(required = false) Long id) {
        ServiceResponse response = productFacade.getAllProductActive(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getProductById(@RequestParam Long id) {
        ServiceResponse response = productService.getOneProductById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping
    public ResponseEntity<?> addProduct(ProductAdminRequestDto dto) {
        ServiceResponse response = productService.addOrEditProduct(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping
    public ResponseEntity<?> editProduct(ProductAdminRequestDto dto) {
        ServiceResponse response = productService.addOrEditProduct(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/status")
    public ResponseEntity<?> statusProduct(@RequestParam Long id) {
        ServiceResponse response = productService.updateStatus(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

}
