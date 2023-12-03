package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.request.speciality.SpeNewRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.SpecialityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/speciality")
@Slf4j
@RequiredArgsConstructor
public class SpecialityAdminController {

    private final SpecialityService specialityService;

    @GetMapping
    public ResponseEntity<?> getAllSpe() {
        ServiceResponse response = specialityService.getAllSpe();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/parent/{productId}")
    public ResponseEntity<?> getParentSpeById(@PathVariable Long productId) {
        ServiceResponse response = specialityService.getParentSpecialityById(productId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/child/{parentId}")
    public ResponseEntity<?> getAllChildSpeByParentId(@PathVariable Long parentId) {
        ServiceResponse response = specialityService.getAllChildSpecialityByParentId(parentId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/new")
    public ResponseEntity<?> addSpeNew(@RequestBody SpeNewRequestDto dto) {
        ServiceResponse response = specialityService.addSpeNew(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/old")
    public ResponseEntity<?> addSpeOld(@RequestBody SpeNewRequestDto dto) {
        ServiceResponse response = specialityService.addSpeOld(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping
    public ResponseEntity<?> editSpeOld(@RequestBody SpeNewRequestDto dto) {
        ServiceResponse response = specialityService.editSpeOld(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        ServiceResponse response = specialityService.updateStatus(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
