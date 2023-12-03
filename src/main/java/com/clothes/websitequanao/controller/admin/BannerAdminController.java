package com.clothes.websitequanao.controller.admin;


import com.clothes.websitequanao.dto.request.BannerAdminRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/banner")
@Slf4j
@RequiredArgsConstructor
public class BannerAdminController {


    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<?> getAllSpe() {
        ServiceResponse response = bannerService.getAllBanner();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBannerById(@PathVariable Long id) {
        ServiceResponse response = bannerService.getBannerById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping
    public ResponseEntity<?> getBannerById(BannerAdminRequestDto dto) {
        ServiceResponse response = bannerService.editBanner(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

}
