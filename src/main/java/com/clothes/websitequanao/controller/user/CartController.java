package com.clothes.websitequanao.controller.user;

import com.clothes.websitequanao.dto.request.cart.CartRequestDTO;
import com.clothes.websitequanao.dto.request.cart.CartRequestNoLoginDTO;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final BillService billService;

    // chưa đăng nhập thêm vào giỏ hàng
    @PostMapping("/api/v1/user/cart")
    public ResponseEntity<?> getAllProductNoLogin(@RequestBody CartRequestNoLoginDTO dto) {
        ServiceResponse result = billService.getAllNoLogin(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    // login thêm product vào cart của user
    @PostMapping("/api/v3/user/cart/localStorage")
    public ResponseEntity<?> addCartIntoCartUserLogin(@RequestBody CartRequestNoLoginDTO dto) {
        ServiceResponse result = billService.addCartLocalstorage(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/api/v3/user/cart")
    public ResponseEntity<?> getAllProductInCart() {
        ServiceResponse result = billService.getAll();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/api/v3/user/cart")
    public ResponseEntity<?> addCart(@RequestBody CartRequestDTO dto) {
        ServiceResponse result = billService.addCart(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/api/v3/user/cart/change/quantity")
    public ResponseEntity<?> changQuantity(@RequestBody CartRequestDTO dto) {
        ServiceResponse result = billService.changQuantity(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/api/v3/user/cart/remove/product")
    public ResponseEntity<?> removeProductOfCart(@RequestBody CartRequestDTO dto) {
        ServiceResponse result = billService.deleteProductOfCart(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/api/v3/user/cart/total")
    public ResponseEntity<?> getTotal() {
        ServiceResponse result = billService.totalCart();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

}
