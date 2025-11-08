package com.comparador.ComparadorTI.user.controller;

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
