package com.clothes.websitequanao.controller;

import com.clothes.websitequanao.dto.request.BrandProductRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;


    @GetMapping("/api/v1/user/brand")
    public ResponseEntity<?> getAllBrand() {
        ServiceResponse response = brandService.findAll();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/api/v1/user/brand")
    public ResponseEntity<?> getProductByBrand(@RequestBody BrandProductRequestDto dto) {
        ServiceResponse response = brandService.getProductByBrand(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/api/v1/user/brand/total")
    public ResponseEntity<?> totalPageBrand(@RequestBody BrandProductRequestDto dto) {
        ServiceResponse response = brandService.getTotalByBrand(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
