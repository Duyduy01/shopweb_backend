package com.clothes.websitequanao.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    private int statusCode;
    private String status;
    private Object[] args;

    public ServiceException(String status, String msg, int statusCode) {
        super(msg);
        this.status = status;
        this.statusCode = statusCode;
    }

    public ServiceException(String status, String msg, int statusCode, Object[] args) {
        super(msg);
        this.status = status;
        this.statusCode = statusCode;
        this.args = args;
    }

    @Override
    public String getMessage() {
        if (args != null) {
            return MessageFormat.format(super.getMessage(), args);
        }
        return super.getMessage();
    }
}
