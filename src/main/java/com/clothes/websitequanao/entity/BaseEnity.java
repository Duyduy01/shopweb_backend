package com.clothes.websitequanao.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id", scope = Long.class)
public class BaseEnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",columnDefinition = "bigint",updatable = false)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;


    private LocalDateTime modifiedDate;


    @CreatedBy
    private String createdBy;

    private String modifiedBy;


}
