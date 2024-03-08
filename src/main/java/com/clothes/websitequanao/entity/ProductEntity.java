package com.clothes.websitequanao.entity;

import com.clothes.websitequanao.utils.CodeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ProductEntity extends BaseEnity {

    private String productName;

    private String productCode;
    private BigDecimal price;
    private BigDecimal priceSell;
    private Integer sale;
    private String description;
    private String content;
    private Long categoryId;
    private Long brandId;
    private Long parentId;
    private String img;
    private Integer active;
    private String type;
    private Integer quantity;
    private Integer sold;
    private Float rate;
    private Integer totalRate;
    private String search;

    @Transient
    private int totalPay;

    @Transient
    private Boolean checkChildren;

    @Transient
    private Boolean boolActive;

    @Transient
    private boolean favorite;

    @Transient
    private Map<Long, List<Object>> mapSpecial;

    @Transient
    private Map<Long, String> mapImg;

    @PrePersist
    private void persistCreatedAt() {
        try {
            String nameSearch = CodeUtil.removeAccent(getProductName());
            this.setCreatedDate(LocalDateTime.now());
            this.setSearch(nameSearch.replaceAll(" ", ""));
        } catch (Exception e) {
            log.error("Unauthorized, error setCreatedAt and setCreatedBy");
            e.printStackTrace();
        }

    }

    @PreUpdate
    private void updateUpdatedAt() {
        try {
            String nameSearch = CodeUtil.removeAccent(getProductName());
            this.setSearch(nameSearch.replaceAll(" ", ""));
            this.setModifiedDate(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Unauthorized, error setUpdatedAt and setUpdatedBy");
            e.printStackTrace();
        }

    }

}
