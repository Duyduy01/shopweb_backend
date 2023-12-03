package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.ImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImgRepo  extends JpaRepository<ImgEntity,Long> {
    List<ImgEntity> findAllByProductId(long id);


    @Query(value = "select ie.img from ImgEntity ie where ie.productId = :id")
    List<String> findAllUrl(Long id);
}
