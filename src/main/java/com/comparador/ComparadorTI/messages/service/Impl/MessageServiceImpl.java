package com.comparador.ComparadorTI.messages.service.Impl;

import com.comparador.ComparadorTI.messages.model.Message;
import com.comparador.ComparadorTI.messages.model.MessageRequest;
import com.comparador.ComparadorTI.messages.repository.MessageRepository;
import com.comparador.ComparadorTI.messages.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    @Override
    public Message getMessage(int id) {
        return messageRepository.getMessage(id);
    }

    @Override
    public Message createMessage(MessageRequest messageRequest) {
        return messageRepository.createMessage(messageRequest);
    }
}
