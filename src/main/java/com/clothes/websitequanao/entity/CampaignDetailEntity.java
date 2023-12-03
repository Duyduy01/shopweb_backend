package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "event_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class CampaignDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private Long eventId;
    private int discount;

    @Transient
    List<ProductEntity> product;
}
