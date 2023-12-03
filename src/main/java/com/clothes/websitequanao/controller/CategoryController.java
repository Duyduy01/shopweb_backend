package com.clothes.websitequanao.controller;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;



    @GetMapping("/api/v1/user/category")
    public ResponseEntity<?> getAllCategory() {
        ServiceResponse response = categoryService.findAllByChild();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/api/v2/user/category")
    public ResponseEntity<?> getAllCategoryV2() {
        ServiceResponse response = categoryService.findAllCate();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
