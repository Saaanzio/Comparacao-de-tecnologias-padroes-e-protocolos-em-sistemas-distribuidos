package com.comparador.ComparadorTI.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UserNotificationController {

    private final Counter messageCounter;
    private final ObjectMapper objectMapper;

    public UserNotificationController(MeterRegistry meterRegistry) {
        this.messageCounter = Counter.builder("websocket.messages.sent")
                .description("Total de mensagens WebSocket enviadas")
                .register(meterRegistry);
        this.objectMapper = new ObjectMapper();
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/users")
    public String sendMessage(String message){
        messageCounter.increment();
        System.out.println(message);
        return message;
    }
}
