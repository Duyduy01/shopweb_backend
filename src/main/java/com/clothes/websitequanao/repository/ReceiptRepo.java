package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepo extends JpaRepository<ReceiptEntity, Long> {

    List<ReceiptEntity> findAllByOrderByReceiptDateDesc();
}
