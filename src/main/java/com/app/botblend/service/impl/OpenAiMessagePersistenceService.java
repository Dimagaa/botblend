package com.app.botblend.service.impl;

import com.app.botblend.client.openai.model.OpenAiMessage;
import com.app.botblend.model.Chat;
import com.app.botblend.model.Message;
import com.app.botblend.repository.ChatRepository;
import com.app.botblend.repository.MessageRepository;
import com.app.botblend.service.MessagePersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class OpenAiMessagePersistenceService implements
        MessagePersistenceService<OpenAiMessage> {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void persist(Long externalId, OpenAiMessage openAiMessage) {
        Chat chat = chatRepository.findByExternalId(externalId)
                .orElseGet(() -> createNewChat(externalId));

        Message message = mapToEntity(chat, openAiMessage);
        messageRepository.save(message);
    }

    private Chat createNewChat(Long externalId) {
        return chatRepository.save(new Chat(externalId));
    }

    private Message mapToEntity(Chat chat, OpenAiMessage openAiMessage) {
        Message message = new Message();
        message.setChat(chat);
        message.setSender(openAiMessage.role());
        message.setContent(openAiMessage.content());
        return message;
    }
}
