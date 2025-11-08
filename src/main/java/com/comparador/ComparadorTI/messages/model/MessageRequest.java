package com.comparador.ComparadorTI.messages.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageRequest(
        @NotBlank
        @Size(min = 3, max = 30)
    String user,
        @NotBlank
    String content
)
{}
