package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;
import java.text.Normalizer;

import javax.persistence.*;

@Data
@Entity
@Table(name = "brand")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BrandEntity extends BaseEnity {

    private String brandName;
    private String brandCode;
    private String content;
    private Integer status;
    private String icon;
    @Transient
    private boolean boolActive;

    @PrePersist
    private void persistNameRemoved() {
        this.setBrandCode(removeAccent(this.brandName));
    }

    @PreUpdate
    private void updateNameRemoved() {
        this.setBrandCode(removeAccent(this.brandName));
    }

    private String removeAccent(String s) {
        s = s.toLowerCase();
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
