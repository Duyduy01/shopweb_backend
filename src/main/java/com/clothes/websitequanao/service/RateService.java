package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.CommentRequestDTO;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface RateService {

    ServiceResponse addReviewProduct(CommentRequestDTO dto);

    ServiceResponse getAllComment(Long productId, Integer page, Integer limit);
}
