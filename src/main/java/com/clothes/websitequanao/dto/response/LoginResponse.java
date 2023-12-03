package com.clothes.websitequanao.dto.response;

import com.clothes.websitequanao.dto.request.AuthUser;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
//    private long id;
//    private String username;
//
//    private String fullName;
//    private String phone;
//    private String status;
//    private Collection<? extends GrantedAuthority> userType;
    private AuthUser user;
}