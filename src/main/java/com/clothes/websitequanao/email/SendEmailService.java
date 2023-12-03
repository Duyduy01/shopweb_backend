package com.clothes.websitequanao.email;

import com.clothes.websitequanao.dto.response.bill.BillResponseDto;

public interface SendEmailService {
    void sendMail(BillResponseDto dto);
}
