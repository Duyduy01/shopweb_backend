package com.clothes.websitequanao.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashedCode {

    private String code;
    private String hash;

    private String secretCode;
    private String qrUri;
    private String deviceId;

    public HashedCode(String code, String hash) {
        this.code = code;
        this.hash = hash;
    }
}