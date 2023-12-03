package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.EmailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public interface MailService {
    void init();

    void sendEmailWelcome(EmailSender emailSender);

    void setMailSender(JavaMailSender javaMailSender);

    @Async
    void sendEmail(EmailSender input);

    void sendBasicEmail(EmailSender emailSender);

    @Async
    void sendMailToSupportTeam(EmailSender input);
}
