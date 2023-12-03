package com.clothes.websitequanao.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeDTO {
    private String type;
    private int expiredTime;
    private String verificationCode;
    private String newApp2FaSecret;
}
