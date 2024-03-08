package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepo extends JpaRepository<BrandEntity,Long> {
    List<BrandEntity> findAllByStatus(int status);

    //check
    boolean existsAllByBrandName(String brandName);

    boolean existsAllByBrandNameAndIdNot(String brandName, Long id);
}
