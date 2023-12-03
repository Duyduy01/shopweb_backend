package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/user-manager/")
@Slf4j
@RequiredArgsConstructor
public class UserManagerController {
    private final UserService userService;

    @GetMapping("/manu")
    public ResponseEntity<?> getUserManu() {
        ServiceResponse response = userService.getAllUserManu();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
