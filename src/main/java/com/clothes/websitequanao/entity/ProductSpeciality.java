package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name = "product_featured")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductSpeciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",columnDefinition = "bigint",updatable = false)
    private Long id;

    private Long featuredId;
    private Long productId;
}
