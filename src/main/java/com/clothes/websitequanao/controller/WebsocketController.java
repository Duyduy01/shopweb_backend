package com.clothes.websitequanao.controller;

import com.clothes.websitequanao.service.BillService;
import com.clothes.websitequanao.exception.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebsocketController {

    private final BillService billService;

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    ExecutorService executorService =
            Executors.newFixedThreadPool(1);
    Future<?> submittedTask;

    @MessageMapping("/billAdded")
    @SendTo("/topic/messages")
    public ResponseEntity<?> billAdded(Long billId) {
        System.out.println("billAdded function is called...");
        if ( submittedTask != null ){
            return new ResponseEntity<>("Task already started", HttpStatus.BAD_REQUEST);
        }
        submittedTask = executorService.submit(() -> {
            while (true) {
                ServiceResponse response = billService.getBillDetailById(billId);
                return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
            }
        });
        return new ResponseEntity<>("Task started", HttpStatus.OK);
    }

    @MessageMapping("/stop")
    @SendTo("/topic/messages")
    public String stopTask() {
        if(submittedTask == null) {
            return "Task not running";
        }
        try {
            submittedTask.cancel(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error occurred while stopping task due to: "
                    + ex.getMessage();
        }
        return "Stopped task";
    }


}
