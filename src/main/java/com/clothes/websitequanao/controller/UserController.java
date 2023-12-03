package com.clothes.websitequanao.controller;

import com.clothes.websitequanao.dto.request.UserDTO;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        userDTO.setEmail(userDTO.getUsername());
        ServiceResponse response = userFacade.register(userDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/sendCode/verify")
    public ResponseEntity<?> sendCode(@RequestBody UserDTO userDTO) {

        ServiceResponse response = userFacade.sendCode(userDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyPhone(@RequestBody UserDTO userDTO) {

        ServiceResponse response = userFacade.verifyEmail(userDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        ServiceResponse tokens = userFacade.login(userDTO);

        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

}
