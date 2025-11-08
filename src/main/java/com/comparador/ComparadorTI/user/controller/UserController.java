package com.comparador.ComparadorTI.user.controller;

import com.comparador.ComparadorTI.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> postUser(@RequestParam String name, @RequestParam String email) {
        return ResponseEntity.ok(userService.createUser(name, email));
    }
}
