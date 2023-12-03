package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.CommentRequestDTO;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.entity.RateEntity;
import com.clothes.websitequanao.entity.ProductBillEntity;
import com.clothes.websitequanao.entity.UserEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.repository.RateRepo;
import com.clothes.websitequanao.repository.ProductBillRepo;
import com.clothes.websitequanao.repository.UserRepo;
import com.clothes.websitequanao.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.clothes.websitequanao.common.Consts.CommentStatus.CMT_SUCCESS;

@Service

@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final RateRepo rateRepo;
    private final UserRepo userRepo;
    private final ProductBillRepo productBillRepo;
    private final ProductRepo productRepo;

    @Override
    public ServiceResponse addReviewProduct(CommentRequestDTO dto) {
        try {
            Long userId = getUserId();
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");
            RateEntity rateEntity = RateEntity.builder()
                    .productId(dto.getProductId())
                    .userId(userId)
                    .stars(dto.getStars())
                    .cmtDatetime(LocalDateTime.now())
                    .context(dto.getContent())
                    .title(dto.getTitle())
                    .build();

            ProductBillEntity productBillEntity = productBillRepo
                    .findByProductIdAndBillId(dto.getProductId(), dto.getBillId()).orElseThrow(() -> new NullPointerException("bill detail does not exist"));

            productBillEntity.setStatus(CMT_SUCCESS);

            productBillRepo.save(productBillEntity);
            rateRepo.save(rateEntity);

            // set rate for product
            ProductEntity product = productRepo.findById(dto.getProductId()).orElseThrow(() -> new NullPointerException("Product do not exist"));

            if (product.getParentId() != null) {
                product = productRepo.findById(product.getParentId()).orElseThrow(() -> new NullPointerException("Product do not exist"));
            }

            // danh sách id
            List<Long> listProductId = productRepo.listIdByChild(product.getId());
            listProductId.add(product.getId());
            // danh sách id

            Float totalStars = 0f;
            int index = 0;
            for (Long productId : listProductId) {
                List<RateEntity> rateEntityList = rateRepo.findAllByProductId(productId);
                index += rateEntityList.size();
                totalStars += rateEntityList.stream().reduce(0f, (total, e) -> total + e.getStars(), Float::sum);
            }

            if (index != 0) {
                Float rate = totalStars / index;
                product.setRate((float) (Math.ceil(rate * 10) / 10));
                product.setTotalRate(product.getTotalRate() + 1);
                productRepo.save(product);
            }

            return ServiceResponse.RESPONSE_SUCCESS("Đánh giá sản phẩm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("Đánh giá sản phẩm thất bại");
        }
    }

    @Override
    public ServiceResponse getAllComment(Long productId, Integer page, Integer limit) {
        try {
            page = page > 0 ? page - 1 : 0;

            Pageable pageable = PageRequest.of(page, limit, Sort.by("cmtDatetime").descending());

            ProductEntity product = productRepo.findById(productId).orElseThrow(() -> new NullPointerException("Product do not exist"));

            if (product.getParentId() != null) {
                product = productRepo.findById(product.getParentId()).orElseThrow(() -> new NullPointerException("Product do not exist"));
            }

            // danh sách id
            List<Long> listProductId = productRepo.listIdByChild(product.getId());
            listProductId.add(product.getId());
            // danh sách id
            /*get cmt product detail all*/
            Page<RateEntity> result = rateRepo.findAllByProductIdIn(listProductId, pageable);

            result.getContent().forEach(e -> {
                UserEntity userName = userRepo.findById(e.getUserId()).orElse(null);
                e.setUserName(userName == null ? "Ẩn danh" : userName.getFullName());
            });


            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("error comment");
        }
    }

    private Long getUserId() {
        Long userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            userId = userRepo.findByUserName(currentUserName).get().getId();
        }
        return userId;
    }
}
