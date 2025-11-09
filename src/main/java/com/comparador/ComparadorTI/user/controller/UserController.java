package com.comparador.ComparadorTI.user.controller;

import com.comparador.ComparadorTI.user.model.User;
import com.comparador.ComparadorTI.user.service.UserService;
import io.micrometer.core.annotation.Timed; // ADD
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final MeterRegistry meterRegistry;
    private final Random random = new Random();

    @Timed(value = "user_controller_requests", extraTags = {"endpoint", "getAllUsers"})
    @GetMapping
    public ResponseEntity<?> getAllUsers() throws InterruptedException {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Timed(value = "user_controller_requests", extraTags = {"endpoint", "getUser"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) throws InterruptedException {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Timed(value = "user_controller_requests", extraTags = {"endpoint", "postUser"})
    @PostMapping("/create")
    public ResponseEntity<?> postUser(@RequestParam String name, @RequestParam String email) throws InterruptedException {
        User created = userService.createUser(name, email);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/health/metrics")
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        meterRegistry.getMeters().forEach(meter -> {
            String name = meter.getId().getName();
            if (name.startsWith("user_controller") || name.startsWith("websocket")) {
                metrics.put(name, meter.measure());
            }
        });

        return metrics;
    }
}