package com.clothes.websitequanao.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.String.format;

@Slf4j
public class ServiceUtils {

    private static final Random random = new Random();
    public static String getRandom6Digits() {
        return format("%06d", random.nextInt(999999));
    }
    public static Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        return props;
    }


    public static String getHash(String val) {
        return StringUtils.isNotBlank(val) ? DigestUtils.sha256Hex(val) : null;
    }

    public static long getTimeAfter(long now, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Time(now));
        cal.add(Calendar.SECOND, second);
        return cal.getTimeInMillis();
    }

    public static boolean isNonNullAndNonZero(Long value) {
        return value != null && value > 0;
    }

    public static boolean isNonNullAndNonZero(Double value) {
        return value != null && value > 0;
    }

    public static boolean isNonNullAndNonZero(BigDecimal value) {
        return value != null && value.compareTo(new BigDecimal(0)) < 1;
    }

    public static boolean isNonNullAndNonZero(Integer value) {
        return value != null && value > 0;
    }


    public static final String numberToTextInEnglish(char number) {
        switch (number) {
            case '0':
                return "zero";
            case '1':
                return "one";
            case '2':
                return "two";
            case '3':
                return "three";
            case '4':
                return "four";
            case '5':
                return "five";
            case '6':
                return "six";
            case '7':
                return "seven";
            case '8':
                return "eight";
            case '9':
                return "nine";
        }
        return null;
    }
}
