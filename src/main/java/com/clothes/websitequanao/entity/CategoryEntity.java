package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@Data
@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity extends BaseEnity {

    private String categoryName;
    private String categoryCode;
    private String content;
    private int status;
    private Long parentId;


    @Transient
    private List<CategoryEntity> listChild;

    @Transient
    private boolean boolActive;

    @PrePersist //được gọi tự động trước khi một entity được persist (lưu trữ lần đầu tiên).
    private void persistNameRemoved() {
        this.setCategoryCode(removeAccent(this.categoryName));
    }

    @PreUpdate //được gọi tự động trước khi một entity được cập nhật trong cơ sở dữ liệu.
    private void updateNameRemoved() {
        this.setCategoryCode(removeAccent(this.categoryName));
    }

    private String removeAccent(String s) {
        s = s.toLowerCase();
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); //chuẩn hóa chuỗi s theo biểu diễn Unicode Normalization Form NFD
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); //Tạo một đối tượng Pattern để tìm kiếm mẫu các ký tự diacritic trong chuỗi
        temp = pattern.matcher(temp).replaceAll(""); //Sử dụng Pattern để tìm và thay thế tất cả các ký tự diacritic trong chuỗi temp bằng chuỗi rỗng, tức là loại bỏ chúng.
        return temp.replaceAll("đ", "d"); // Loại bỏ ký tự 'đ' (đấu) bằng 'd' trong chuỗi temp
    }


}
