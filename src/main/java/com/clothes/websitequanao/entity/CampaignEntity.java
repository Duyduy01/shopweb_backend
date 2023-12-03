package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.clothes.websitequanao.common.Consts.campaignType.CAMPAIGN_UNFINISHED;

@Data
@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class CampaignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String campaignName;
    private String content;
    private LocalDate startDay;
    private LocalDate endDay;
    private int status;

    @PrePersist
    private void persistCreatedAt() {
        try {
            this.setCreatedDate(LocalDateTime.now());
            this.setStatus(CAMPAIGN_UNFINISHED);
        } catch (Exception e) {
            log.error("Unauthorized, error setCreatedAt and setCreatedBy");
            e.printStackTrace();
        }
    }

    @PreUpdate
    private void updateUpdatedAt() {
        try {
            this.setModifiedDate(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Unauthorized, error setUpdatedAt and setUpdatedBy");
            e.printStackTrace();
        }

    }

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Long createdBy;

    private Long modifiedBy;
}
