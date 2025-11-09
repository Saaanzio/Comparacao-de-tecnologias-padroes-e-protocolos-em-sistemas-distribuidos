package com.comparador.ComparadorTI.events.controller;

import com.comparador.ComparadorTI.events.service.EventService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@AllArgsConstructor
public class EventController {
    private final EventService eventService;
    private final MeterRegistry meterRegistry;

    @Timed(value = "event_controller_requests", extraTags = {"endpoint", "getAllEvents"})
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @Timed(value = "event_controller_requests", extraTags = {"endpoint", "createEvent"})
    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestParam String type, @RequestParam String source) {
        return ResponseEntity.ok(eventService.createEvent(type, source));
    }

    @Timed(value = "event_controller_requests", extraTags = {"endpoint", "getEvent"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @Timed(value = "event_controller_requests", extraTags = {"endpoint", "updateStatus"})
    @PatchMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable int id) {
        return ResponseEntity.ok(eventService.updateStatus(id));
    }

    @GetMapping("/health/metrics")
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        meterRegistry.getMeters().forEach(meter -> {
            String name = meter.getId().getName();
            if (name.startsWith("event_controller")) {
                metrics.put(name, meter.measure());
            }
        });

        return metrics;
    }
}
