package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.common.Consts;
import com.clothes.websitequanao.common.ServiceUtils;
import com.clothes.websitequanao.dto.request.EmailSender;
import com.clothes.websitequanao.dto.request.HashedCode;
import com.clothes.websitequanao.dto.request.VerifyCodeDTO;
import com.clothes.websitequanao.entity.TwoFactorCode;
import com.clothes.websitequanao.entity.UserEntity;
import com.clothes.websitequanao.repository.TwoFactorCodeRepo;
import com.clothes.websitequanao.service.MailService;
import com.clothes.websitequanao.service.SystemParamService;
import com.clothes.websitequanao.service.TwoFactorService;
import com.twilio.Twilio;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static com.clothes.websitequanao.WebsiteQuanAoApplication.SYSTEM_PARAM_MAP;
import static com.clothes.websitequanao.common.Consts.Status.ACTIVE;
import static com.clothes.websitequanao.common.Consts.SystemParamKey.*;
import static com.clothes.websitequanao.common.ServiceUtils.getHash;
import static com.clothes.websitequanao.common.ServiceUtils.getTimeAfter;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Slf4j
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TwoFactorServiceImpl implements TwoFactorService {

    private JavaMailSender javaMailSender;
    private static String twillioAccountSid;
    private static String twillioAuthToken;
    private static String twillioFromPhoneNumber;
    //stringee
    private String stringeeApiUrl;
    private String stringeeFromNumber;
    private String stringeeAuthToken;
    private static URI SMS_WEBHOOK_URI = null;
    private final DefaultCodeVerifier codeVerifier;

    //email
    private String sendgridFromEmail;

    private static final List<String> PREFIX_VOICE_CALL_ONLY = Arrays.asList("90", "93", "89", "70", "79", "77", "76", "78");
    private static String VOICE_CALL_TEMPLATE = "{\n" + "    \"from\": {\n" + "        \"type\": \"external\",\n" + "        \"number\": \"${stringeeFromNumber}\",\n" + "        \"alias\": \"${stringeeFromNumber}\"\n" + "    },\n" + "    \"to\": [{\n" + "        \"type\": \"external\",\n" + "        \"number\": \"${stringeeToNumber}\",\n" + "        \"alias\": \"${stringeeToNumber}\"\n" + "    }],\n" + "    \"actions\": [{\n" + "        \"action\": \"talk\",\n" + "        \"voice\": \"female\",\n" + "        \"speed\": -2,\n" + "        \"loop\": 2,\n" + "        \"text\": \"${stringeeText}\"\n" + "    }]\n" + "}";

    private SystemParamService systemParamService;

    private TwoFactorCodeRepo twoFactorCodeRepo;

    private MailService mailService;

    @Autowired
    public TwoFactorServiceImpl(SystemParamService systemParamService, TwoFactorCodeRepo twoFactorCodeRepo,
                                MailService mailService) {
        this.twoFactorCodeRepo = twoFactorCodeRepo;

        this.systemParamService = systemParamService;

        this.mailService = mailService;
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        codeVerifier.setAllowedTimePeriodDiscrepancy(0);
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void init() {
        twillioAccountSid = SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_TWILIO_ACCOUNTSID);
        twillioAuthToken = SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_TWILIO_AUTHTOKEN);
        twillioFromPhoneNumber = SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_TWILIO_FROMPHONENUMBER);

        sendgridFromEmail = SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_SENDGRID_MAIL_FROM);

        stringeeApiUrl = SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_STRINGEE_VOICECALL_APIURL);
        stringeeFromNumber = SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_STRINGEE_VOICECALL_FROMNUMBER);
        stringeeAuthToken = SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_STRINGEE_VOICECALL_AUTHTOKEN);

        Twilio.init(twillioAccountSid, twillioAuthToken);
        VOICE_CALL_TEMPLATE = VOICE_CALL_TEMPLATE.replace("${stringeeFromNumber}", stringeeFromNumber.replace("+", ""));
    }

    @Override
    public TwoFactorCode generateVerifyCode(UserEntity user, VerifyCodeDTO verifyCodeDTO) {
        String faType = verifyCodeDTO.getType();

        HashedCode hashedCode = null;

        String code = ServiceUtils.getRandom6Digits();
        hashedCode = new HashedCode(code, getHash(code));

        int timeExpire = 600;
        if (verifyCodeDTO.getExpiredTime() > 0) {
            timeExpire = verifyCodeDTO.getExpiredTime();
        } else {
            String sys = systemParamService.getSystemByKey(SYSTEM_PARAM_VERIFICATION_CODE_EXPIRATION_TIME);
            if (!isBlank(sys)) {
                timeExpire = Integer.parseInt(sys);
            }
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        TwoFactorCode twoFactorCode = TwoFactorCode.builder().userId(user.getId())
                .type(faType)
                .email(user.getEmail())
//                .phone(user.getPhone()).countryCode(user.getCountryCode())
                .verificationCode(hashedCode.getCode())
                .status(ACTIVE)
                .createdAt(now)
                .expiredAt(new Timestamp(getTimeAfter(now.getTime(), timeExpire)))
                .hashedCode(hashedCode).build();

        twoFactorCode = twoFactorCodeRepo.save(twoFactorCode);
        return twoFactorCode;
    }

    @Override
    public void sendVerificationCode(TwoFactorCode factorCode) {
        factorCode.setType(isBlank(factorCode.getType()) ? Consts.FactorAuthenType.EMAIL : factorCode.getType().toUpperCase());
        try {
            switch (factorCode.getType()) {
                case Consts.FactorAuthenType.NONE:
                case Consts.FactorAuthenType.EMAIL:
                    EmailSender emailSender = EmailSender.builder()
                            .recipientEmail(factorCode.getEmail())
                            .code(factorCode.getVerificationCode())
                            .fullName(factorCode.getFullName())
                            .reason(factorCode.getReasonSendEmail())
                            .new2faType(factorCode.getNew2faType())
                            .reasonIds(factorCode.getReasonIds())
                            .otherRejectReason(factorCode.getOtherRejectReason())
                            .check2fa(factorCode.isCheck2fa())
                            .build();
                    mailService.sendEmail(emailSender);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("Fail to send verification code");
            e.printStackTrace();
        }
    }

    @Override
    public String getVerificationCode(long id) {
        Optional<TwoFactorCode> twoFactorCode = twoFactorCodeRepo.findTop1ByUserIdOrderByCreatedAtDesc(id);

        if (twoFactorCode.isPresent()) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            int check = now.compareTo(twoFactorCode.get().getExpiredAt());
            if (check > 0) return null;
            return twoFactorCode.get().getVerificationCode();
        }
        return null;
    }

    @Override
    public void deleteVerifyCodeByUserId(long userId) {
        try {
            this.twoFactorCodeRepo.deleteByUserId(userId);
        } catch (Exception e) {
            log.error("Error clear verification code");
            e.printStackTrace();
        }
    }


}
