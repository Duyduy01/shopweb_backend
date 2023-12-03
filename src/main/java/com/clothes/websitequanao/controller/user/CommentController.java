package com.clothes.websitequanao.controller.user;

import com.clothes.websitequanao.dto.request.CommentRequestDTO;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.RateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor

public class CommentController {

    private final RateService rateService;

    @PostMapping("/api/v3/user/comment/review")
    public ResponseEntity<?> addReviewProduct(@RequestBody CommentRequestDTO dto) {
        ServiceResponse result = rateService.addReviewProduct(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/api/v1/user/comment/{productId}")
    public ResponseEntity<?> getAllBillByStatus(@PathVariable Long productId,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "5") Integer limit) {
        ServiceResponse result = rateService.getAllComment(productId, page, limit);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
