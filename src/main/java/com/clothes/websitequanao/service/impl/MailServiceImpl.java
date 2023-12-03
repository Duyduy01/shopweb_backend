package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.common.Consts.*;
import com.clothes.websitequanao.dto.request.EmailSender;
import org.springframework.mail.javamail.JavaMailSender;
import com.clothes.websitequanao.common.Consts;
import com.clothes.websitequanao.exception.ErrorCodes;
import com.clothes.websitequanao.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


import static com.clothes.websitequanao.WebsiteQuanAoApplication.SYSTEM_PARAM_MAP;
import static com.clothes.websitequanao.common.Consts.SystemParamKey.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;



@Slf4j
@Service
public class MailServiceImpl implements MailService {

    JavaMailSender mailSender;
    TemplateEngine templateEngine;
    private static String fromEmail; //todo change here use system param

    private static String color;
    private static String supportEmail;
    private static String tokenName;
    private static String linkDownloadIOS;
    private static String linkDownloadAndroid;
    private static String tokenTerm;
    private static String currency;
    private static String backgroundColor;

    private static String EMAIL_TEMPLATE = null;
    private static String EMAIL_TEMPLATE_TRANSACTION = null;
    private static String EMAIL_TEMPLATE_WELCOME = null;
    private static String EMAIL_TEMPLATE_KYC_APPROVED = null;
    private static String EMAIL_TEMPLATE_INVITE = null;
    private static String EMAIL_TEMPLATE_RESET_2FA_DEFAULT = null;
    private static String EMAIL_TEMPLATE_CC_SUPPORT = null;
    private static String EMAIL_TEMPLATE_UNLOCK = null;

    private static final String FOR_DEFAULT = "Hi ${fullName},<br>This is your verification code:";
    private static final String FOR_SEND_CODE_TRANSFER = "Hi ${fullName},<br>Please use the following code to confirm transfer request";
    private static final String FOR_SEND_CODE_WITHDRAW = "Hi ${fullName},<br>Please use the following code to confirm withdraw request";
    private static final String FOR_REGISTER = "Dear ${fullName},<br>Thank you for your registration with ${tokenName}.<br>Please use the following code to activate your account:";
    private static final String FOR_LOGIN = "Hi ${fullName},<br>Please use the following code to login:";
    private static final String FOR_RESET_PASSWORD = "Hi ${fullName},<br>You are receiving this email because we received a password reset request for your account. Please use the following code to confirm your request:";
    private static final String FOR_FORGOT_PIN = "Hi ${fullName},<br>You are receiving this email because we received a PIN reset request for your account. Please use the following code to confirm your request:";
    private static final String FOR_CHANGE_PIN = "Hi ${fullName},<br>You are receiving this email because we received a PIN change request for your account. Please use the following code to confirm your request:";
    private static final String FOR_CHANGE_2FA = "Hi ${fullName},<br>You are receiving this email because we received a 2FA change request for your account. Please use the following code to confirm your request:";
    private static final String FOR_CHANGE_2FA_SUCCESS = "Hi ${fullName},<br>Your ${tokenName} 2FA type has been changed to ${new2faType}.";
    private static final String FOR_CHANGE_2FA_TURN_OFF_SUCCESS = "Hi ${fullName},<br>Your ${tokenName} 2FA has been turn off.";
    private static final String FOR_CHANGE_2FA_TURN_ON_SUCCESS = "Hi ${fullName},<br>Your ${tokenName} 2FA has been turn on.";
    private static final String FOR_CHANGE_PIN_SUCCESS = "Hi ${fullName},<br>Your ${tokenName} PIN has been changed.";
    private static final String FOR_CHANGE_PASSWORD_SUCCESS = "Hi ${fullName},<br>Your ${tokenName} password has been changed.";
    private static final String FOR_TURNOFF_2FA = "Hi ${fullName},<br>You are receiving this email because we received a turn off 2FA request. Please use the following code to confirm your request:";
    private static final String FOR_TURNON_2FA = "Hi ${fullName},<br>You are receiving this email because we received a turn on 2FA request. Please use the following code to confirm your request:";
    private static final String FOR_APPROVE_KYC = "Hi ${fullName},<br>Your KYC request has been approved. You can transfer and withdraw now.";
    private static final String FOR_REJECT_KYC = "Hi ${fullName},<br>Your KYC request has been rejected. Please check the following reasons and update your personal information again.<ul class=\"mb-16\" style=\"padding: 10px 27px; background-color: #FBEDEE; \">${reasons}</ul>";

    private static final String SUBJECT_FOR_DEFAULT = "[${tokenName}] Your Verification Code";
    private static final String SUBJECT_FOR_REGISTER = "[${tokenName}] Please verify Your Email Address";
    private static final String SUBJECT_FOR_LOGIN = "[${tokenName}] Login to ${tokenName}";
    private static final String SUBJECT_FOR_RESET_PASSWORD = "[${tokenName}] Resetting Your Password";
    private static final String SUBJECT_FOR_FORGOT_PIN = "[${tokenName}] Resetting Your PIN";
    private static final String SUBJECT_FOR_CHANGE_PIN = "[${tokenName}] Request change Your PIN";
    private static final String SUBJECT_FOR_CHANGE_2FA = "[${tokenName}] Request change Your 2FA";
    private static final String SUBJECT_FOR_CHANGE_2FA_SUCCESS = "[${tokenName}] Your 2FA type has been changed";
    private static final String SUBJECT_FOR_CHANGE_PIN_SUCCESS = "[${tokenName}] Your PIN has been changed";
    private static final String SUBJECT_FOR_CHANGE_PASSWORD_SUCCESS = "[${tokenName}] Your password has been changed";
    private static final String SUBJECT_FOR_WELCOME = "[${tokenName}] Welcome to ${tokenName}";
    private static final String SUBJECT_FOR_INVITE = "[${tokenName}] You’re invited to ${tokenName}";
    private static final String SUBJECT_FOR_UNLOCK = "[${tokenName}] Your Account Is Unlocked";

    private static final String SUBJECT_FOR_TURNOFF_2FA = "[${tokenName}] Request turn off your 2FA to login";
    private static final String SUBJECT_FOR_TURNON_2FA = "[${tokenName}] Request turn on your 2FA to login";
    private static final String SUBJECT_FOR_TURNOFF_2FA_SUCCESS = "[${tokenName}] Turn off 2FA to login successfully";
    private static final String SUBJECT_FOR_TURNON_2FA_SUCCESS = "[${tokenName}] Turn on 2FA to login successfully";
    private static final String SUBJECT_FOR_APPROVE_KYC = "[${tokenName}] Your KYC request has been approved";
    private static final String SUBJECT_FOR_REJECT_KYC = "[${tokenName}] Your KYC request has been rejected";

    private static final String SUBJECT_FOR_RESET_2FA_DEFAULT = "[${tokenName}] You’re request reset 2FA to default";
    private static final String SUBJECT_FOR_CHANGE_PHONE_BY_ADMIN = "[${tokenName}] You’re request change phone";
    private static final String SUBJECT_FOR_SEND_CODE_TRANSFER = "[${tokenName}] Verify Transfer Request";
    private static final String SUBJECT_FOR_SEND_CODE_WITHDRAW = "[${tokenName}] Verify Withdraw Request";

    private static final String DOWNLOAD_FOOTER = "Get the latest ${tokenName} App for your phone";

    @Autowired
    public MailServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    @Override
    public void init() {
        fromEmail = SYSTEM_PARAM_MAP().get(SystemParamKey.SYSTEM_TOKEN_CURRENCY_NAME) + " <" + SYSTEM_PARAM_MAP().get(SYSTEM_PARAM_SENDGRID_MAIL_FROM) + ">";
        color = SYSTEM_PARAM_MAP().get(SystemParamKey.SYSTEM_TOKEN_CURRENCY_COLOR);
        supportEmail = SYSTEM_PARAM_MAP().get(SystemParamKey.SYSTEM_TOKEN_CURRENCY_SUPPORT_EMAIL);
        tokenName = SYSTEM_PARAM_MAP().get(SystemParamKey.SYSTEM_TOKEN_CURRENCY_NAME);
        linkDownloadIOS = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_APP_IOS_DOWNLOAD);
        linkDownloadAndroid = SYSTEM_PARAM_MAP().get(SystemParamKey.SYSTEM_TOKEN_APP_ANDROID_DOWNLOAD);
        tokenTerm = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_TERMS);
        currency = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY);
        backgroundColor = SYSTEM_PARAM_MAP().get(SystemParamKey.SYSTEM_TOKEN_BACKGROUND_COLOR);

        EMAIL_TEMPLATE = this.initTemplateData("templates/en_mail_notification_template.html");
        EMAIL_TEMPLATE_TRANSACTION = this.initTemplateData("templates/en_email_template_transaction.html");
        EMAIL_TEMPLATE_WELCOME = this.initTemplateData("templates/en_email_template_welcpme.html");
        EMAIL_TEMPLATE_KYC_APPROVED = this.initTemplateData("templates/en_email_template_verified.html");
        EMAIL_TEMPLATE_INVITE = this.initTemplateData("templates/en_email_template_invite.html");
        EMAIL_TEMPLATE_RESET_2FA_DEFAULT = this.initTemplateData("templates/en_email_template_reset2fa.html");
        EMAIL_TEMPLATE_CC_SUPPORT = this.initTemplateData("templates/en_mail_template_cc_to_support.html");
        EMAIL_TEMPLATE_UNLOCK = this.initTemplateData("templates/en_email_template_unlock.html");

    }

    @Async
    @Override
    public void sendEmailWelcome(EmailSender input) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setTo(input.getRecipientEmail());
            mimeMessageHelper.setFrom(fromEmail);
//            mimeMessageHelper.setCc(SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_ADMIN_EMAIL));

            String tokenName = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_NAME);
            String content = EMAIL_TEMPLATE_WELCOME;

            String subject = SUBJECT_FOR_WELCOME.replace("${tokenName}", tokenName.toUpperCase());
            mimeMessageHelper.setSubject(subject);

            mimeMessageHelper.setText(content, true);
            // Send email
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Fail to send welcome email");
            e.printStackTrace();
        }
        // Send mail to Marketing team
        input.setReason(Consts.SendEmailReason.FOR_WELCOME);
        sendMailToSupportTeam(input);
    }

    @Override
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailSender input) {
        ErrorCodes err = ErrorCodes.BAD_REQUEST;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setTo(input.getRecipientEmail());
            mimeMessageHelper.setFrom(fromEmail);

            String tokenName = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_NAME);
            String content = EMAIL_TEMPLATE.replace("${code}", isNotBlank(input.getCode()) ? input.getCode() : "");
            content = content.replace("${codeColor}", SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_CODE_COLOR));
            String subject = null;

            switch (input.getReason()) {
                case Consts.SendEmailReason.FOR_REGISTER:

                    content = content.replace("${fullname_subject}", FOR_REGISTER.replace("${fullName}", input.getFullName()));
                    subject = SUBJECT_FOR_REGISTER.replace("${tokenName}", tokenName.toUpperCase());
//                    mimeMessageHelper.setCc(new String[]{SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_MARKETING_EMAIL), SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_ADMIN_EMAIL)});
//                    sendMailToSupportTeam(input);
                    break;
                case Consts.SendEmailReason.FOR_LOGIN:
                    content = content.replace("${fullname_subject}", FOR_LOGIN.replace("${fullName}", input.getFullName()));
                    subject = SUBJECT_FOR_LOGIN.replace("${tokenName}", tokenName.toUpperCase());
                    break;
                default:
                    content = content.replace("${fullname_subject}", FOR_DEFAULT.replace("${fullName}", input.getFullName()));
                    subject = SUBJECT_FOR_DEFAULT.replace("${tokenName}", tokenName.toUpperCase());
                    break;
            }
            content = content.replace("${tokenName}", SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_NAME));
            content = content.replace("${displayButton}", "none");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);
            // Send email
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Fail to send security email");
            e.printStackTrace();
        }
    }

    @Override
    public void sendBasicEmail(EmailSender emailSender) {

    }

    @Override
    public void sendMailToSupportTeam(EmailSender input) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            String sendTo = null;
            mimeMessageHelper.setFrom(fromEmail);

            String tokenName = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_NAME);
            String content = EMAIL_TEMPLATE_CC_SUPPORT;
            String subject = null;
            switch (input.getReason()) {
                case Consts.SendEmailReason.FOR_REGISTER:
                    sendTo = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_MARKETING_EMAIL);
                    subject = "[${tokenName}] ${fullname} has just registered a new account!"
                            .replace("${fullname}", input.getFullName())
                            .replace("${tokenName}", tokenName);
                    content = content.replace("${content_detail}", "has just registered a new account!");
                    content = content.replace("${teamName}", "Marketing");
                    break;
                case Consts.SendEmailReason.FOR_WELCOME:
                    sendTo = SYSTEM_PARAM_MAP().get(SYSTEM_TOKEN_CURRENCY_MARKETING_EMAIL);
                    subject = "[${tokenName}] Welcome ${fullname} to ${tokenName}"
                            .replace("${fullname}", input.getFullName())
                            .replace("${tokenName}", tokenName);
                    content = content.replace("${content_detail}", "has just successfully created ${tokenName} account." .replace("${tokenName}", tokenName));
                    content = content.replace("${teamName}", "Marketing");
                    break;
                default:
                    break;
            }
            content = content.replace("${fullname}", input.getFullName()).replace("${email}", input.getRecipientEmail());
            content = content.replace("${tokenName}", tokenName);

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);

            // Send email
            if (sendTo != null) {
                mimeMessageHelper.setTo(sendTo);
                mailSender.send(mimeMessage);
            }
        } catch (Exception e) {
            log.error("Fail to send email to support team");
            e.printStackTrace();
        }
    }
    private String initTemplateData(String templatePath) {
        //todo lấy ảnh Verified từ param ${tokenVerifiedImage}
        InputStream inputKycApprove = getClass().getClassLoader().getResourceAsStream(templatePath);
        BufferedReader bufferedKycApprove = new BufferedReader(new InputStreamReader(inputKycApprove, StandardCharsets.UTF_8));
        return bufferedKycApprove.lines().collect(Collectors.joining("\n"))
                .replace("${tokenColor}", color)
                .replace("${backgroundColor}", backgroundColor)
                .replace("${tokenName}", tokenName)
                .replace("${tokenNameFooter}", DOWNLOAD_FOOTER.replace("${tokenName}", tokenName))
                .replace("${linkDownloadIOS}", linkDownloadIOS)
                .replace("${supportEmail}", supportEmail)
                .replace("${linkDownloadAndroid}", linkDownloadAndroid)
                .replace("${tokenTerm}", tokenTerm)
//                .replace("${startNowLink}", SYSTEM_PARAM_MAP().get(SYSTEM_KYC_URL))
//                .replace("${startVerifyLink}", SYSTEM_PARAM_MAP().get(SYSTEM_KYC_URL))
                .replace("${tokenVerifiedImage}", "CLEVERME" .equalsIgnoreCase(tokenName) ?
                        "https://res.cloudinary.com/storeimage/image/upload/v1637329032/verified-clm_csbbhr.png" :
                        "https://res.cloudinary.com/storeimage/image/upload/v1636428402/verified_tmnnm1.png");
    }
}
