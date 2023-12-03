package com.clothes.websitequanao.controller.user;

import com.clothes.websitequanao.dto.request.manager.UseInfoRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v3/user")
public class ManagerController {
    private final UserService userService;

    @PostMapping("/detail")
    public ResponseEntity<?> updateProductUser(@RequestBody UseInfoRequestDto dto) {

        ServiceResponse result = userService.updateUserInfo(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
