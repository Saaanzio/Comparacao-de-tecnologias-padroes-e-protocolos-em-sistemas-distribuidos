package com.comparador.ComparadorTI.messages.repository.Impl;

import com.comparador.ComparadorTI.messages.model.Message;
import com.comparador.ComparadorTI.messages.model.MessageRequest;
import com.comparador.ComparadorTI.messages.repository.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private static Integer ID = 0;
    private final HashMap<Integer, Message> messageHashMap = new HashMap<>();
    @Override
    public Message getMessage(int id) {
        return Optional.ofNullable(messageHashMap.get(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Message createMessage(MessageRequest request) {
        Message message = new Message(ID, request.user(), request.content(), LocalDateTime.now());
        messageHashMap.put(ID, message);
        ID += 1;
        return message;
    }
}
