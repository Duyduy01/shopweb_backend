package com.clothes.websitequanao.facade.impl;

import com.clothes.websitequanao.facade.TwoFaFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwoFaFacadeImpl implements TwoFaFacade {
    private String twillioAccountSid;

    public void setTwillioAccountSid(String twillioAccountSid) {
        this.twillioAccountSid = twillioAccountSid;
    }
}
