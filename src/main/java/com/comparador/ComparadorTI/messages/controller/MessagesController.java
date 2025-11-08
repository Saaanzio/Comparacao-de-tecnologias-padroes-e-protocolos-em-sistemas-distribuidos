package com.comparador.ComparadorTI.messages.controller;

import com.comparador.ComparadorTI.messages.model.MessageRequest;
import com.comparador.ComparadorTI.messages.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class MessagesController {
    private final MessageService messageService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessage(@PathVariable int id) {
        return ResponseEntity.ok(messageService.getMessage(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> postMessage(@Valid @RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(messageService.createMessage(messageRequest));
    }
}
