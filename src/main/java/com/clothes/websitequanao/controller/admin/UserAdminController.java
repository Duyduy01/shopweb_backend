package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.request.UserAdminRequestDTO;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/supper/admin")
@Slf4j
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping("/customer/{status}")
    public ResponseEntity<?> getCustomer(@PathVariable Integer status) {
        ServiceResponse response = userService.getCustomer(status);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/customer/add")
    public ResponseEntity<?> addCustomer(@RequestBody UserAdminRequestDTO dto) {
        ServiceResponse response = userService.addCustomer(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/customer/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        ServiceResponse response = userService.updateStatus(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
