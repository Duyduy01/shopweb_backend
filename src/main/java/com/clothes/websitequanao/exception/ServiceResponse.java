package com.clothes.websitequanao.exception;

import lombok.*;
import org.apache.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {

    private String status;
    private int statusCode;
    private String message;
    private Object data;

    public static ServiceResponse RESPONSE_SUCCESS(Object data) {
        ServiceResponse response = new ServiceResponse();
        response.setStatusCode(HttpStatus.SC_OK);
        response.setMessage(ErrorCodes.SUCCESS.message());
        response.setStatus(ErrorCodes.SUCCESS.status());
        response.setData(data);
        return response;
    }

    public static ServiceResponse RESPONSE_SUCCESS(String message, Object data) {
        ServiceResponse response = new ServiceResponse();
        response.setStatusCode(HttpStatus.SC_OK);
        response.setMessage(message);
        response.setStatus(ErrorCodes.SUCCESS.status());
        response.setData(data);
        return response;
    }

    public static ServiceResponse RESPONSE_ERROR(Object data) {
        ServiceResponse response = new ServiceResponse();
        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        response.setMessage(ErrorCodes.BAD_REQUEST.message());
        response.setStatus(ErrorCodes.BAD_REQUEST.status());
        response.setData(data);
        return response;
    }

    public static ServiceResponse RESPONSE_ERROR(String message, Object data) {
        ServiceResponse response = new ServiceResponse();
        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        response.setMessage(message);
        response.setStatus(ErrorCodes.BAD_REQUEST.status());
        response.setData(data);
        return response;
    }

}
