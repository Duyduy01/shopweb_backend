package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.CampaignDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampaignDetailRepo extends JpaRepository<CampaignDetailEntity, Long> {

    @Query(value = "select cd.productId from CampaignDetailEntity cd where cd.eventId = :eventId")
    List<String> getProductId(Long eventId);


    List<CampaignDetailEntity> findAllByEventId(Long id);
}
