package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepo extends JpaRepository<CampaignEntity, Long> {
    List<CampaignEntity> findAllByOrderByCreatedDateDesc();


    List<CampaignEntity> findAllByStatusIn(List<Integer> status);
}
