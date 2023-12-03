package com.clothes.websitequanao.controller;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/user/banner")
    public ResponseEntity<?> getAllBannerByCode(@RequestParam String code){
        ServiceResponse response = bannerService.getAllBannerByCode(code);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }



}
