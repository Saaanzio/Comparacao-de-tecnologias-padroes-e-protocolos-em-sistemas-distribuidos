package com.comparador.ComparadorTI.events.controller;

import com.comparador.ComparadorTI.events.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestParam String type, @RequestParam String source ) {
        return ResponseEntity.ok(eventService.createEvent(type, source));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }
    @PatchMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable int id) {
        return ResponseEntity.ok(eventService.updateStatus(id));
    }
}
