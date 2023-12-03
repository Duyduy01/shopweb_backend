package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "feedback")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateEntity extends BaseEnity {

    private Long productId;
    private Integer stars;
    private String title;
    private Long userId;
    private LocalDateTime cmtDatetime;
    private String context;

    @Transient
    private String userName;
}
