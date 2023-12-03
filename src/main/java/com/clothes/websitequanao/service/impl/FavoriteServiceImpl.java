package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.entity.FavoriteEntity;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.FavoriteRepo;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.repository.UserRepo;
import com.clothes.websitequanao.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static com.clothes.websitequanao.common.Consts.FunctionStatus.ON;
import static com.clothes.websitequanao.common.Consts.favoriteType.DISLIKE;
import static com.clothes.websitequanao.common.Consts.favoriteType.LIKE;

@Slf4j
@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final FavoriteRepo favoriteRepo;

    private final EntityManager entityManager;

    @Override
    public ServiceResponse favoriteProduct(Long productId) {
        try {
            Long userId = getUserId();

            boolean checkExist = favoriteRepo.existsByProductIdAndUserId(productId, userId);

            if (checkExist) {
                favoriteRepo.deleteByProductIdAndUserId(productId, userId);
                return ServiceResponse.RESPONSE_SUCCESS(DISLIKE);
            } else {
                favoriteRepo.save(FavoriteEntity.builder().productId(productId).userId(userId).build());
                return ServiceResponse.RESPONSE_SUCCESS(LIKE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi favorite product");
            return ServiceResponse.RESPONSE_ERROR("Lỗi yêu thích sản phẩm");
        }

    }

    @Override
    public ServiceResponse getFavoriteProduct(Integer page, Integer limit) {
        try {
            List<ProductEntity> result = new ArrayList<>();
            Long userId = getUserId();

            page = (page > 0 ? page - 1 : 0) * limit;

            String sql = " select p.* from product p join favorite f on p.id = f.product_id where f.user_id = :userId and active = :active order by f.created_date desc limit :page,:limit ";

            Query query = entityManager.createNativeQuery(sql, ProductEntity.class);
            query.setParameter("userId", userId);
            query.setParameter("active", ON);
            query.setParameter("page", page);
            query.setParameter("limit", limit);
            result = query.getResultList();

            setFavorite(result);
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi favorite product");
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra sản phẩm  yêu thích");
        }
    }

    @Override
    public ServiceResponse totalFavoriteProduct() {
        try {
            Long userId = getUserId();

            int total = favoriteRepo.countAllByUserId(userId);

            int result = (int) Math.ceil(((float) total) / 5);

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi total favorite product");
            return ServiceResponse.RESPONSE_ERROR(0);
        }
    }

    @Override
    public ServiceResponse quantityFavoriteProduct() {
        try {
            Long userId = getUserId();

            int total = favoriteRepo.countAllByUserId(userId);

            return ServiceResponse.RESPONSE_SUCCESS(total);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi quantity favorite product");
            return ServiceResponse.RESPONSE_ERROR(0);
        }
    }

    @Override
    public void setFavorite(List<ProductEntity> productEntityList) {
        Long userId = getUserId();
        if (userId != null) {
            productEntityList.forEach(e -> {
                e.setFavorite(favoriteRepo.existsByProductIdAndUserId(e.getId(), userId));
            });
        }
    }

    @Override
    public boolean getFavoriteProductDetail(Long productId) {
        Long userId = getUserId();
        if (userId != null) {
            return favoriteRepo.existsByProductIdAndUserId(productId, userId);
        }
        return false;
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
