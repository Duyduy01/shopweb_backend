package com.clothes.websitequanao.entity;

import com.clothes.websitequanao.dto.request.HashedCode;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "two_factor_code")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class TwoFactorCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String type;
    private String email;
    private String phone;
    private String countryCode;
    private String verificationCode;
    private String status;
    private Timestamp createdAt;
    private Timestamp expiredAt;
    private String sid;


    @Transient
    private HashedCode hashedCode;
    @Transient
    private String fullName;
    @Transient
    private String reasonSendEmail;
    @Transient
    private String password;
    @Transient
    private String new2faType;
    @Transient
    private boolean check2fa;
    @Transient
    private String reasonIds;
    @Transient
    private String otherRejectReason;
}
