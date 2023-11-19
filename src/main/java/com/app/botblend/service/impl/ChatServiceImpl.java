package com.app.botblend.service.impl;

import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import com.app.botblend.mapper.ChatMapper;
import com.app.botblend.mapper.MessageMapper;
import com.app.botblend.model.Chat;
import com.app.botblend.model.Message;
import com.app.botblend.repository.ChatRepository;
import com.app.botblend.repository.MessageRepository;
import com.app.botblend.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;

    @Transactional
    @Override
    public ChatDto sendMessage(Long id, MessageSendRequestDto requestDto) {
        Chat chat = chatRepository.findById(id).orElseThrow();
        Message message = messageMapper.toModel(requestDto, chat);
        messageRepository.save(message);
        return chatMapper.toDto(chat);
    }

    @Override
    public List<ChatDto> findAll() {
        return chatRepository.findAll()
                .stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Override
    public ChatDto getById(Long id) {
        return chatRepository.findById(id)
                .map(chatMapper::toDto)
                .orElseThrow();
    }
}