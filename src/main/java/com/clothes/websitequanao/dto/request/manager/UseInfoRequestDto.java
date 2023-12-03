package com.clothes.websitequanao.dto.request.manager;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data

public class UseInfoRequestDto {
    @NotBlank
    private String fullName;
    @NotBlank
    private String phone;
    @NotBlank
    private String birthday;
    @NotBlank
    private Integer gender;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotBlank
    private String ward;
    @NotBlank
    private String address;
}
