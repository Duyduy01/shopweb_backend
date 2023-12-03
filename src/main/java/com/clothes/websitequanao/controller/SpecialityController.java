package com.clothes.websitequanao.controller;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.SpecialityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService specialityService;


    @GetMapping("/api/v1/user/speciality")
    public ResponseEntity<?> getSpe() {
        ServiceResponse response = specialityService.getSpeciality();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/api/v1/user/speciality/key")
    public ResponseEntity<?> getName() {
        ServiceResponse response = specialityService.getName();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/api/v1/user/speciality/{productId}")
    public ResponseEntity<?> getSpeById(@PathVariable Long productId) {
        ServiceResponse response = specialityService.getSpecialityById(productId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
