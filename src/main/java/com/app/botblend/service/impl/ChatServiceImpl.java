package com.app.botblend.service.impl;

import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.dto.chat.MessageDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import com.app.botblend.exception.EntityNotFoundException;
import com.app.botblend.mapper.ChatMapper;
import com.app.botblend.mapper.MessageMapper;
import com.app.botblend.model.Chat;
import com.app.botblend.model.Message;
import com.app.botblend.repository.ChatRepository;
import com.app.botblend.repository.MessageRepository;
import com.app.botblend.service.ChatService;
import com.app.botblend.service.MessageSenderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private final MessageSenderService messageSenderService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;

    @Transactional
    @Override
    public MessageDto sendMessage(Long id, MessageSendRequestDto requestDto) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot send message: Not found chat with id: " + id
                ));

        Message message = messageMapper.toModel(requestDto, chat);
        messageRepository.save(message);
        messageSenderService.sendMessage(message);

        return messageMapper.toDto(message);
    }

    @Override
    public List<ChatDto> findAll(Pageable pageable) {
        return chatRepository.findAll(pageable)
                .stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Override
    public List<MessageDto> getMessagesByChatId(Long chatId, Pageable pageable) {
        return messageRepository.findAllByChatId(chatId, pageable)
                .stream()
                .map(messageMapper::toDto)
                .toList();

    }
}
