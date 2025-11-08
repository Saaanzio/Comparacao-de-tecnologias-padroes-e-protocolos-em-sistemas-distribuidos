package com.comparador.ComparadorTI.events.service.Impl;

import com.comparador.ComparadorTI.events.model.Event;
import com.comparador.ComparadorTI.events.repository.EventRepository;
import com.comparador.ComparadorTI.events.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public Event getEvent(int id) {
        return eventRepository.getEvent(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    @Override
    public Event createEvent(String type, String source) {
        return eventRepository.createEvent(type, source);
    }

    @Override
    public String updateStatus(int id) {
        return eventRepository.updateStatus(id);
    }
}
