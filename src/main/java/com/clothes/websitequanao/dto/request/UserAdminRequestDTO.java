package com.clothes.websitequanao.dto.request;

import lombok.Data;

@Data
public class UserAdminRequestDTO {
    private String fullName;
    private String phone;
    private String password;
    private String repeatPassword;
    private String email;
    private String birthday;
    private Integer gender;
    private String city;
    private String district;
    private String ward;
    private String address;

    private String type;
}
