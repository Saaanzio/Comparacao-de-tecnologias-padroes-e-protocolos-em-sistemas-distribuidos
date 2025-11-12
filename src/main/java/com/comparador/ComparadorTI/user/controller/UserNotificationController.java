package com.comparador.ComparadorTI.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UserNotificationController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/users")
    public String sendMessage(String message){
        System.out.println(message);
        return message;
    }
}
