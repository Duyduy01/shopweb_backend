package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table(name = "featured")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityEntity extends BaseEnity implements Comparable<SpecialityEntity> {

    private String featuredName;
    private int featuredNumber;
    private String description;
    private String featuredCode;
    private String featuredKey;
    private int active;
    private Integer featuredPosition;


    @Transient
    private boolean boolActive;


    @Transient
    private boolean checkParent;

    @Override
    public int compareTo(SpecialityEntity o) {
        return featuredPosition.compareTo(o.featuredPosition);
    }

}
