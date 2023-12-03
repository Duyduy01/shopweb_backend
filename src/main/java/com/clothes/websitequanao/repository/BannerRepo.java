package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface BannerRepo extends JpaRepository<BannerEntity, Long> {

    List<BannerEntity> findAllByBannerCode(String code);


    //admin
    @Query(value = "select b.bannerCode from BannerEntity b ")
    Set<String> getBannerCode();

    //admin
}
