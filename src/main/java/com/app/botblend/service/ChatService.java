package com.app.botblend.service;

import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import java.util.List;

public interface ChatService {
    ChatDto sendMessage(Long chatId, MessageSendRequestDto requestDto);

    List<ChatDto> findAll();

    ChatDto getById(Long id);
}
