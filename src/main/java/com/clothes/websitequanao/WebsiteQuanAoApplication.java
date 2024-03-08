package com.clothes.websitequanao;

import com.clothes.websitequanao.entity.SystemParam;
import com.clothes.websitequanao.facade.impl.TwoFaFacadeImpl;
import com.clothes.websitequanao.repository.ProductReceiptRepo;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.repository.SystemParamRepo;
import com.clothes.websitequanao.service.MailService;
import com.clothes.websitequanao.service.impl.TwoFactorServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.*;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.MAP_PARAM_SECURITIES_CONFIG;
import static com.clothes.websitequanao.common.Consts.SystemParamKey.*;
import static java.lang.Integer.parseInt;

@Slf4j
@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class WebsiteQuanAoApplication implements CommandLineRunner {
    public static String segmentApiKey = "";
    public static Map<String, String> SLACK_PARAM_MAP = new HashMap<>();


    private static Map<String, String> SYSTEM_PARAM_MAP = new HashMap<>();


    public static Map<String, String> SYSTEM_BO_SETTINGS = new HashMap<>();

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebsiteQuanAoApplication.class, args);
        MailService mailService = (MailService) context.getBean("mailServiceImpl");

        SystemParamRepo systemParamRepo = (SystemParamRepo) context.getBean("systemParamRepo");

        TwoFactorServiceImpl twoFactorServiceImpl = (TwoFactorServiceImpl) context.getBean("twoFactorServiceImpl");

        TwoFaFacadeImpl twoFaFacadeImpl = (TwoFaFacadeImpl) context.getBean("twoFaFacadeImpl");

        //System param - MainDB
        List<SystemParam> securitySystemParam = systemParamRepo.findByKeys(Arrays.asList(SYSTEM_KYC_URL,
                SYSTEM_PARAM_NUM_OF_RESEND_CODE,
                SYSTEM_TOKEN_CURRENCY,
                SYSTEM_TOKEN_CURRENCY_NAME,
                SYSTEM_TOKEN_CURRENCY_LOGO,
                SYSTEM_TOKEN_CURRENCY_COLOR,
                SYSTEM_TOKEN_CURRENCY_CODE_COLOR,
                SYSTEM_TOKEN_BACKGROUND_COLOR,
                SYSTEM_TOKEN_CURRENCY_FAVICON,
                SYSTEM_TOKEN_CURRENCY_SUPPORT_EMAIL,
                SYSTEM_TOKEN_CURRENCY_ADMIN_EMAIL,
                SYSTEM_TOKEN_CURRENCY_MARKETING_EMAIL,
                SYSTEM_TOKEN_APP_ANDROID_DOWNLOAD,
                SYSTEM_TOKEN_APP_IOS_DOWNLOAD,
                SYSTEM_TOKEN_TERMS,
                SYSTEM_WEB_BASE_URL,

                //EMAIL, SMS, VOICECALL
                SYSTEM_API_WEBHOOK_URL_TWILLIO_SMS,
                SYSTEM_PARAM_STRINGEE_ACCOUNTSID,
                SYSTEM_PARAM_STRINGEE_AUTHTOKEN,
                SYSTEM_PARAM_STRINGEE_VOICECALL_FROMNUMBER,
                SYSTEM_PARAM_STRINGEE_VOICECALL_AUTHTOKEN,
                SYSTEM_PARAM_STRINGEE_VOICECALL_APIURL,

                SYSTEM_PARAM_TWILIO_ACCOUNTSID,
                SYSTEM_PARAM_TWILIO_AUTHTOKEN,
                SYSTEM_PARAM_TWILIO_FROMPHONENUMBER,

                SYSTEM_PARAM_SENDGRID_MAIL_HOST,
                SYSTEM_PARAM_SENDGRID_MAIL_PORT,
                SYSTEM_PARAM_SENDGRID_MAIL_USERNAME,
                SYSTEM_PARAM_SENDGRID_MAIL_PASSWORD,
                SYSTEM_PARAM_SENDGRID_MAIL_FROM,

                SYSTEM_PARAM_GOOGLE_RECAPTCHA_ENDPOINT,
                SYSTEM_PARAM_GOOGLE_RECAPTCHA_SERVER_KEY

        ));

        SYSTEM_PARAM_MAP.putAll(securitySystemParam.stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())));

        List<SystemParam> securityParams = systemParamRepo.findByKeys(Arrays.asList(
                SYSTEM_PATH_NETWORK_ADDRESS_CONFIG,
                SYSTEM_PATH_PUBLIC_KEY,
                SYSTEM_PATH_PRIVATE_KEY
        ));
        MAP_PARAM_SECURITIES_CONFIG.putAll(securityParams.stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())));

        /*========================Call init methods========================*/


        twoFaFacadeImpl.setTwillioAccountSid(SYSTEM_PARAM_MAP.get(SYSTEM_PARAM_TWILIO_ACCOUNTSID));
        //Mail
        JavaMailSender javaMailSender = getJavaMailSender(SYSTEM_PARAM_MAP);
        mailService.setMailSender(javaMailSender);
        mailService.init();

//        Stringee, twillio
        twoFactorServiceImpl.setJavaMailSender(javaMailSender);
        twoFactorServiceImpl.init();

        // BO - Settings - Params
        SYSTEM_BO_SETTINGS = systemParamRepo.findByKeys(Arrays.asList(SYSTEM_PARAM_SETTING_RESET_2FA_URL,
                        SYSTEM_PARAM_SETTING_RESET_2FA_TIME_EXPIRE,
                        SYSTEM_PARAM_SETTING_CHANGE_PHONE_URL,
                        SYSTEM_PARAM_SETTING_CHANGE_PHONE_TIME_EXPIRE))
                .stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
    }


    public static Map<String, String> SYSTEM_PARAM_MAP() {
        return SYSTEM_PARAM_MAP;
    }


    public static JavaMailSender getJavaMailSender(Map<String, String> params) {
        JavaMailSenderImpl mailSender = new org.springframework.mail.javamail.JavaMailSenderImpl();
        mailSender.setHost(params.get(SYSTEM_PARAM_SENDGRID_MAIL_HOST));
        mailSender.setPort(parseInt(params.get(SYSTEM_PARAM_SENDGRID_MAIL_PORT)));
        mailSender.setUsername(params.get(SYSTEM_PARAM_SENDGRID_MAIL_USERNAME));
        mailSender.setPassword(params.get(SYSTEM_PARAM_SENDGRID_MAIL_PASSWORD));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(props);

        return mailSender;

    }

    @Bean
    PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedOriginPatterns(Collections.singletonList("*")); // <-- you may change "*"
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Arrays.asList(
//                "Accept", "Origin", "Content-Type", "Depth", "User-Agent", "If-Modified-Since,",
//                "Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With", "X-File-Name"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    private final ProductReceiptRepo productReceiptRepo;


    private final ProductRepo productRepo;

    @Override
    public void run(String... args) throws Exception {
//        List<ProductEntity> productEntities = productRepo.findAllByParentId(null);
//        productEntities.forEach(e -> {
//            e.setNameSearch(CodeUtil.removeAccent(e.getProductName()).replaceAll(" ", ""));
//        });
//        productRepo.saveAll(productEntities);
    }
}

