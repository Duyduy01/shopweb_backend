package com.clothes.websitequanao.exception;

import org.apache.http.HttpStatus;

public enum ErrorCodes {

    SUCCESS("SUCCESS", "Success", HttpStatus.SC_OK),
    BAD_REQUEST("BAD_REQUEST", "Bad request", HttpStatus.SC_BAD_REQUEST),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized", HttpStatus.SC_UNAUTHORIZED),
    FORBIDDEN("FORBIDDEN", "Forbidden", HttpStatus.SC_FORBIDDEN),

    INVALID_CAPTCHA("INVALID_CAPTCHA", "Invalid captcha", HttpStatus.SC_BAD_REQUEST),
    REQUIRE_CAPTCHA("REQUIRE_CAPTCHA", "Require captcha", HttpStatus.SC_BAD_REQUEST),
    INVALID_VERIFICATION("INVALID_VERIFICATION", "Invalid verification code", HttpStatus.SC_BAD_REQUEST),
    REQUIRE_VERIFICATION("REQUIRE_VERIFICATION", "Require verification code", HttpStatus.SC_BAD_REQUEST),
    EXPIRED_VERIFICATION("EXPIRED_VERIFICATION", "Expired verification code", HttpStatus.SC_BAD_REQUEST),
    APP_2FA_IS_DISABLE("APP_2FA_IS_DISABLE", "App Authenticator is disabled", HttpStatus.SC_BAD_REQUEST),

    USER_LOCKED("USER_LOCKED", "User locked", HttpStatus.SC_BAD_REQUEST),
    USER_EXPIRED("USER_EXPIRED", "User expired", HttpStatus.SC_BAD_REQUEST),
    USER_BLOCKED("USER_BLOCKED", "The user has been blocked. Please try again later!", HttpStatus.SC_BAD_REQUEST),
    USER_SUSPECTED("USER_SUSPECTED", "Your account has been locked. Contact your support person to unlock it, then try again.", HttpStatus.SC_BAD_REQUEST),
    USER_NOT_EXISTS("USER_NOT_EXISTS", "User not exists", HttpStatus.SC_BAD_REQUEST),
    ACCOUNT_ALREADY_EXISTS("ACCOUNT_ALREADY_EXISTS", "Account already exists", HttpStatus.SC_BAD_REQUEST),
    INVALID_USER_PASSWORD("INVALID_USER_PASSWORD", "Username or password is incorrect", HttpStatus.SC_BAD_REQUEST),

    BLANK_EMAIL("BLANK_EMAIL", "Blank email", HttpStatus.SC_BAD_REQUEST),
    INVALID_EMAIL("INVALID_EMAIL", "Invalid email", HttpStatus.SC_BAD_REQUEST),
    BLANK_PHONE("BLANK_PHONE", "Blank phone", HttpStatus.SC_BAD_REQUEST),
    INVALID_PHONE("INVALID_PHONE", "Invalid phone", HttpStatus.SC_BAD_REQUEST),
    BLANK_PASSWORD("BLANK_PASSWORD", "Blank password", HttpStatus.SC_BAD_REQUEST),
    INVALID_PASSWORD("INVALID_PASSWORD", "Invalid password", HttpStatus.SC_BAD_REQUEST),
    WEAK_PASSWORD("WEAK_PASSWORD", "Weak password", HttpStatus.SC_BAD_REQUEST),
    BLANK_NAME("BLANK_NAME", "Blank name", HttpStatus.SC_BAD_REQUEST),
    INVALID_NAME("INVALID_NAME", "Invalid name", HttpStatus.SC_BAD_REQUEST),
    NOT_CONFIRM("NOT_CONFIRM", "Not confirm", HttpStatus.SC_BAD_REQUEST),
    BLANK_COUNTRY("BLANK_COUNTRY", "Blank country", HttpStatus.SC_BAD_REQUEST),
    INVALID_COUNTRY_CODE("INVALID_COUNTRY_CODE", "Invalid country code", HttpStatus.SC_BAD_REQUEST),
    BLANK_NATIONALITY("BLANK_NATIONALITY", "Blank nationality", HttpStatus.SC_BAD_REQUEST),
    BLANK_ADDRESS("BLANK_ADDRESS", "Blank address", HttpStatus.SC_BAD_REQUEST),
    BLANK_CITY("BLANK_CITY", "Blank city", HttpStatus.SC_BAD_REQUEST),
    BLANK_STATE("BLANK_STATE", "Blank state", HttpStatus.SC_BAD_REQUEST),
    WRONG_OLD_PASSWORD("WRONG_OLD_PASSWORD", "Wrong old password", HttpStatus.SC_BAD_REQUEST),
    DUPLICATE_OLD_PASSWORD("DUPLICATE_OLD_PASSWORD", "Duplicate old password", HttpStatus.SC_BAD_REQUEST),
    INVALID_PIN("INVALID_PIN", "Invalid Pin", HttpStatus.SC_BAD_REQUEST),
    WRONG_OLD_PIN("WRONG_OLD_PIN", "Wrong old pin", HttpStatus.SC_BAD_REQUEST),
    CANNOT_BE_SAME_PIN("CANNOT_BE_SAME_PIN", "Old pin and new pin cannot be same.", HttpStatus.SC_BAD_REQUEST),
    INVALID_BONUS_PROGRAM_REQUEST("INVALID_BONUS_PROGRAM_REQUEST", "Invalid bonus program request", HttpStatus.SC_BAD_REQUEST),

    INVALID_STATUS("INVALID_STATUS", "Invalid status", HttpStatus.SC_BAD_REQUEST),
    INVALID_POST_TYPE("INVALID_POST_TYPE", "Invalid post type", HttpStatus.SC_BAD_REQUEST),
    INVALID_IS_PIN("INVALID_IS_PIN", "Invalid is pin", HttpStatus.SC_BAD_REQUEST),
    INVALID_IS_PUBLIC("INVALID_IS_PUBLIC", "Invalid is public", HttpStatus.SC_BAD_REQUEST),
    NULL_SCHEDULE_TIME("NULL_SCHEDULE_TIME", "Schedule time is null", HttpStatus.SC_BAD_REQUEST),
    INVALID_SCHEDULE_TIME("INVALID_SCHEDULE_TIME", "Schedule time is before current time", HttpStatus.SC_BAD_REQUEST),

    INVALID_SORT_TYPE("INVALID_SORT_TYPE", "Invalid sort type", HttpStatus.SC_BAD_REQUEST),
    INVALID_SORT_BY("INVALID_SORT_BY", "Invalid sortBy", HttpStatus.SC_BAD_REQUEST);

    private int statusCode;
    private String message;
    private String status;

    ErrorCodes() {
    }

    ErrorCodes(String status, String message, int statusCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
    }

    public int statusCode() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    public String status() {
        return status;
    }
}
