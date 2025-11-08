package com.comparador.ComparadorTI.events.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Event {int id; EventType type; String source; EventStatus status; LocalDateTime date;}

