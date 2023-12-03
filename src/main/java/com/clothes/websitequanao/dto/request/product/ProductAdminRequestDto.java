package com.clothes.websitequanao.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAdminRequestDto {

    private Long id;

    @NotBlank(message = "nhập tên sản phẩm")
    private String productName;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @PositiveOrZero
    private BigDecimal price;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @PositiveOrZero
    private BigDecimal priceSell;


    @NotBlank(message = "nhập mã sản phẩm")
    private String productCode;

    @NotBlank(message = "nhập giá mô tả ngắn")
    private String description;

    @NotBlank(message = "nhập giá mô tả")
    private String content;

//    @NotBlank(message = "nhập loại sản phẩm")
    private Long categoryId;

//    @NotBlank(message = "nhập loại sản phẩm")
    private Long brandId;

    private Long parentId;

    private MultipartFile img;

    private List<MultipartFile> listImg;

    private List<Long> listIdDelete;

    @NotNull
    private Integer active;


    private List<Long> specialityId;

    @AssertTrue(message = "giá nhập phải nhỏ hơn giá bán")
    private boolean isValidCheckPrice() {
        if (this.priceSell.compareTo(BigDecimal.ZERO) == 0
                && this.price.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        else {
            return Objects.nonNull(this.priceSell) &&
                    Objects.nonNull(this.price) && (this.price.compareTo(this.priceSell)<0);
        }

    }
}
