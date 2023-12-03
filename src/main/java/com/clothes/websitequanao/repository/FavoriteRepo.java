package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface FavoriteRepo extends JpaRepository<FavoriteEntity, Long> {
    boolean existsByProductIdAndUserId(Long productId, Long userId);

    @Modifying
    @Transactional
    void deleteByProductIdAndUserId(Long productId, Long userId);


    int countAllByUserId(Long userId);
}
