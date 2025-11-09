package com.comparador.ComparadorTI.messages.controller;

import com.comparador.ComparadorTI.messages.model.MessageRequest;
import com.comparador.ComparadorTI.messages.service.MessageService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class MessagesController {
    private final MessageService messageService;
    private final MeterRegistry meterRegistry;
    @Timed(value = "messages_controller_requests", extraTags = {"endpoint", "getMessage"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessage(@PathVariable int id) {
        return ResponseEntity.ok(messageService.getMessage(id));
    }

    @Timed(value = "messages_controller_requests", extraTags = {"endpoint", "postMessage"})
    @PostMapping("/create")
    public ResponseEntity<?> postMessage(@Valid @RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(messageService.createMessage(messageRequest));
    }

    @GetMapping("/health/metrics")
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        meterRegistry.getMeters().forEach(meter -> {
            String name = meter.getId().getName();
            if (name.startsWith("messages_controller")) {
                metrics.put(name, meter.measure());
            }
        });

        return metrics;
    }
}
