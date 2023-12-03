package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.request.brand.BrandAdminRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/brand")
@Slf4j
@RequiredArgsConstructor
public class BrandAdminController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<?> getAllReceipt() {
        ServiceResponse response = brandService.getAllBrand();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }


    @PostMapping
    public ResponseEntity<?> addBrand(BrandAdminRequestDto dto) {
        ServiceResponse response = brandService.addOrEditBrand(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }


    @PutMapping
    public ResponseEntity<?> editBrand(BrandAdminRequestDto dto) {
        ServiceResponse response = brandService.addOrEditBrand(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        ServiceResponse response = brandService.updateStatus(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}