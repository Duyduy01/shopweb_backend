//package com.clothes.doanwebsitequanaodaiduong.exception.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class ServiceExceptionHandler {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @ExceptionHandler(ServiceException.class)
//    public void handler(HttpServletRequest request, HttpServletResponse response, ServiceException serviceException) throws IOException {
//        Map<String, Object> resp = new LinkedHashMap<>();
//        resp.put("status", serviceException.getStatus());
//        resp.put("statusCode", serviceException.getStatusCode());
//        resp.put("message", serviceException.getMessage());
//        resp.put("data", serviceException.getArgs());
//
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(serviceException.getStatusCode());
//        response.getWriter().write(objectMapper.writeValueAsString(resp));
//    }
//}
