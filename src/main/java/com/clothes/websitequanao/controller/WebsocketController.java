package com.clothes.websitequanao.controller;

import com.clothes.websitequanao.dto.response.bill.BillidDto;
import com.clothes.websitequanao.service.BillService;
import com.clothes.websitequanao.exception.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebsocketController {

    private final BillService billService;

    ExecutorService executorService =
            Executors.newFixedThreadPool(1);
    Future<?> submittedTask;

    @MessageMapping("/billAdded")
    @SendTo("/topic/new-billAdded")
    public ResponseEntity<?> billAdded(@Payload BillidDto billidDto) {
        Long billId = billidDto.getBillId();
        System.out.println("billAdded function is called...");
        if (submittedTask != null) {
            return new ResponseEntity<>("Task already started", HttpStatus.BAD_REQUEST);
        }
//        submittedTask = executorService.submit(() -> {
//            while (true) {
        ServiceResponse response = billService.getNewBillAdded(billId);
        return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        });
    }

    @MessageMapping("/stop")
    @SendTo("/topic/messages")
    public String stopTask() {
        if (submittedTask == null) {
            return "Task not running";
        }
        try {
            submittedTask.cancel(true);
            return "Stopped task";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error occurred while stopping task due to: "
                    + ex.getMessage();
        }
    }
}
