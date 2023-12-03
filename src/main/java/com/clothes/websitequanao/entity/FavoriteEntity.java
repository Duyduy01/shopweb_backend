package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "favorite")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long userId;

    private LocalDateTime createdDate;


    @PrePersist
    private void persistCreatedAt() {
        try {
            this.setCreatedDate(LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

