package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.request.category.CateAdminRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/category")
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    @GetMapping("/parent")
    public ResponseEntity<?> getParentCategory() {
        ServiceResponse response = categoryService.getAllParentId();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping
    public ResponseEntity<?> addCate(@RequestBody CateAdminRequestDto dto) {
        ServiceResponse response = categoryService.addOrEditCate(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping
    public ResponseEntity<?> editCate(@RequestBody CateAdminRequestDto dto) {
        ServiceResponse response = categoryService.addOrEditCate(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
    @GetMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        ServiceResponse response = categoryService.updateStatus(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

}

