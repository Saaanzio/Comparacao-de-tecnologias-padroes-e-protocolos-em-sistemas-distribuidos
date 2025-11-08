package com.comparador.ComparadorTI.events.repository;

import com.comparador.ComparadorTI.events.model.Event;

import java.util.List;

public interface EventRepository {
    Event getEvent(int id);
    List<Event> getAllEvents();
    Event createEvent(String type, String source);
    String updateStatus(int id);
}
