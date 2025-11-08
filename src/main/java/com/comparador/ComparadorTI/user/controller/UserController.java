package com.comparador.ComparadorTI.user.controller;

import com.comparador.ComparadorTI.user.model.User;
import com.comparador.ComparadorTI.user.service.UserService;
import io.micrometer.core.annotation.Timed; // ADD
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;


    @Timed(value = "user.controller.requests", extraTags = {"endpoint", "getAllUsers"})
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        messagingTemplate.convertAndSend("/topic/users", "All users requested");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Timed(value = "user.controller.requests", extraTags = {"endpoint", "getUser"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        messagingTemplate.convertAndSend("/topic/users", "User requested with ID: " + id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Timed(value = "user.controller.requests", extraTags = {"endpoint", "postUser"})
    @PostMapping("/create")
    public ResponseEntity<?> postUser(@RequestParam String name, @RequestParam String email) {
        User created = userService.createUser(name, email);
        messagingTemplate.convertAndSend("/topic/users", created);
        return ResponseEntity.ok(created);
    }
}