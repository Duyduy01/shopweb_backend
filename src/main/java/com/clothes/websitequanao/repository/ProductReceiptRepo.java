package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.ProductReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReceiptRepo extends JpaRepository<ProductReceiptEntity,Long> {
    List<ProductReceiptEntity> findAllByReceiptId(Long id);
}
