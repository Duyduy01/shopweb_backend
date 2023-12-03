package com.clothes.websitequanao.controller.user;


import com.clothes.websitequanao.dto.request.product.ProductDetailRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/product-detail")
public class ProductDetailController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllProduct(@PathVariable Long id) {
        ServiceResponse response = productService.getProductDetailById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping
    public ResponseEntity<?> getProductBySpe(@RequestBody ProductDetailRequestDto dto) {
        ServiceResponse result = productService.getProductDetailBySpe(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
