package com.comparador.ComparadorTI.events.repository.Impl;

import com.comparador.ComparadorTI.events.model.Event;
import com.comparador.ComparadorTI.events.model.EventStatus;
import com.comparador.ComparadorTI.events.model.EventType;
import com.comparador.ComparadorTI.events.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EventRepositoryImpl implements EventRepository {
    private static Integer ID = 0;
    private final ConcurrentHashMap<Integer, Event> eventHashMap = new ConcurrentHashMap<>();
    @Override
    public Event getEvent(int id) {
        return Optional.ofNullable(eventHashMap.get(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(eventHashMap.values());
    }

    @Override
    public Event createEvent(String type, String source) {
        try {
            EventType eventType = EventType.valueOf(type.toUpperCase());
            Event event = new Event(ID, eventType, source, EventStatus.ENABLED, LocalDateTime.now());
            eventHashMap.put(ID, event);
            ID++;
            return event;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid event type");
        }
    }

    @Override
    public String updateStatus(int id) {
        String result = "";
        if(eventHashMap.containsKey(id)) {
            Event event = eventHashMap.get(id);
            if (event.getStatus() == EventStatus.ENABLED) {
                event.setStatus(EventStatus.DISABLED);
                eventHashMap.put(id, event);
                result = "Evento desativado com sucesso.";
            }
            else{
                event.setStatus(EventStatus.ENABLED);
                eventHashMap.put(id, event);
                result = "Evento ativado com sucesso.";
            }
        }
        return result;
    }
}
