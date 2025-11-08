package com.comparador.ComparadorTI.messages.model;

import java.time.LocalDateTime;

public record Message(
        int id,
        String user,
        String content,
        LocalDateTime timestamp
)
{
}
