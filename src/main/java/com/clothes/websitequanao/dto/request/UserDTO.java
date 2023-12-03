package com.clothes.websitequanao.dto.request;

import com.clothes.websitequanao.entity.UserEntity;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private String verificationCode;
    private String username;
    private String email;
    private String phone;
    private String password;
//    private String firstName;
//    private String lastName;
    private String fullName;
    private String address;
    private String refreshToken;


    public UserDTO(UserEntity user) {
        this.username = user.getUserName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.password = user.getPassword();
//        this.firstName = user.getFirstName();
//        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.address = user.getAddress();
    }
    public static UserDTO buildProfile(UserEntity user) {
        return new UserDTO(user);
    }


}
