package com.app.botblend.service;

import com.app.botblend.dto.chat.ChatDto;
import com.app.botblend.dto.chat.MessageDto;
import com.app.botblend.dto.chat.MessageSendRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ChatService {
    MessageDto sendMessage(Long chatId, MessageSendRequestDto requestDto);

    List<ChatDto> findAll(Pageable pageable);

    List<MessageDto> getMessagesByChatId(Long id, Pageable pageable);
}
