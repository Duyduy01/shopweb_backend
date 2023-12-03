package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.RateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepo extends JpaRepository<RateEntity,Long> {
    Page<RateEntity> findAllByProductIdIn(List<Long> productId, Pageable pageable);


    List<RateEntity> findAllByProductId(Long productId);
}
