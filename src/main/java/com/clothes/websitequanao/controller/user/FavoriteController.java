package com.clothes.websitequanao.controller.user;

import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/user")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("/favorite")
    public ResponseEntity<?> favoriteProduct(@RequestParam Long productId) {
        ServiceResponse result = favoriteService.favoriteProduct(productId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }



    @GetMapping("/favorite-info")
    public ResponseEntity<?> billInfo(@RequestParam Integer page,
                                      @RequestParam Integer limit) {
        ServiceResponse result = favoriteService.getFavoriteProduct(page,limit);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/favorite-info/total")
    public ResponseEntity<?> totalBill() {
        ServiceResponse result = favoriteService.totalFavoriteProduct();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/favorite-info/quantity")
    public ResponseEntity<?> quantityFavorite() {
        ServiceResponse result = favoriteService.quantityFavoriteProduct();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
