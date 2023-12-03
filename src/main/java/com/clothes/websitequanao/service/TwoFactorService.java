package com.clothes.websitequanao.service;


import com.clothes.websitequanao.dto.request.VerifyCodeDTO;
import com.clothes.websitequanao.entity.TwoFactorCode;
import com.clothes.websitequanao.entity.UserEntity;

public interface TwoFactorService {
    TwoFactorCode generateVerifyCode(UserEntity user, VerifyCodeDTO verifyCodeDTO);

    void sendVerificationCode(TwoFactorCode factorCode);

    String getVerificationCode(long id);

    void deleteVerifyCodeByUserId(long userId);
}
