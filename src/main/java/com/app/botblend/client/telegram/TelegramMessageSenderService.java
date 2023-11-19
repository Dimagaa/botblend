package com.app.botblend.client.telegram;

import com.app.botblend.model.Message;
import com.app.botblend.service.MessageSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
@Service
public class TelegramMessageSenderService implements MessageSenderService {
    private final TelegramBotClient telegramBotClient;

    @Override
    public boolean sendMessage(Message message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChat().getId())
                .text(message.getContent())
                .build();
        telegramBotClient.executeMethod(sendMessage);
        return true;
    }
}
