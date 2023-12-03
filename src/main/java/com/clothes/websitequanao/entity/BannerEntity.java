package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "banner")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerEntity extends BaseEnity {

    private String bannerName;
    private String bannerCode;
    private String img;
    private String bannerType;
    private Integer number;
    private String bannerLink;
}
