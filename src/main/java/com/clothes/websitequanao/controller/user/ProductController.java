package com.clothes.websitequanao.controller.user;

import com.clothes.websitequanao.dto.request.product.ProductRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.facade.ProductFacade;
import com.clothes.websitequanao.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/product")
public class ProductController {

    private final ProductFacade productFacade;

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProductParent() {
        ServiceResponse response = productFacade.getAllProduct(null);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterProduct(@RequestBody Map<String, List<String>> dto) {
        ServiceResponse response = productService.filterProduct(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/filter/total/page")
    public ResponseEntity<?> filterTotalPage(@RequestBody Map<String, List<String>> dto) {
        ServiceResponse response = productService.filterTotalPage(dto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestBody ProductRequestDto dto) {
        ServiceResponse result = productService.searchProductByName(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @PostMapping("/search/total")
    public ResponseEntity<?> searchTotalProduct(@RequestBody ProductRequestDto dto) {
        ServiceResponse result = productService.totalProductBySearch(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/search/totalPage")
    public ResponseEntity<?> totalResultProduct(@RequestBody ProductRequestDto dto) {
        ServiceResponse result = productService.totalPageProductBySearch(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
