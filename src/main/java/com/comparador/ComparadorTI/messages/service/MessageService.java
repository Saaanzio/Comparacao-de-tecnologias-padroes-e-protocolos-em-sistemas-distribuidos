package com.comparador.ComparadorTI.messages.service;

import com.comparador.ComparadorTI.messages.model.Message;
import com.comparador.ComparadorTI.messages.model.MessageRequest;

public interface MessageService {
    Message getMessage(int id);
    Message createMessage(MessageRequest messageRequest);
}
