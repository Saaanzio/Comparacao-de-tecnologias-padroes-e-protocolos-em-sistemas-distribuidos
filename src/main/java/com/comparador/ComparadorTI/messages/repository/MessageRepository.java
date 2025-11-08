package com.comparador.ComparadorTI.messages.repository;

import com.comparador.ComparadorTI.messages.model.Message;
import com.comparador.ComparadorTI.messages.model.MessageRequest;

public interface MessageRepository {
    Message getMessage(int id);
    Message createMessage(MessageRequest request);
}
