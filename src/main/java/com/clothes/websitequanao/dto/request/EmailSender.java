package com.clothes.websitequanao.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSender {
    private String recipientEmail;
    private String code;
    private String fullName;
    private String subject;
    private String sendFrom;
    private String reason;
    private String content;
    private String new2faType;
    private String reasonIds;
    private String otherRejectReason;
    private boolean check2fa;

    private String transactionStatus;
    private String transactionId;
    private String transactionDate;
    private String transactionType;
    private String transactionAmount;
    private String transactionCurrency;

    private String transactionAmountDollar;
    private String transactionCurrencyName;

    private Integer expireTime; // hours
}
