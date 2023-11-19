package com.app.botblend.client.telegram;

import com.app.botblend.client.telegram.handlers.MessageHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class TelegramMessageHandlerManager {
    private final List<MessageHandler> messageHandlers;

    public BotApiMethodMessage handleUpdate(Update update) {
        return messageHandlers.stream()
                .filter(handler -> handler.isSupport(update))
                .map(handler -> handler.handle(update))
                .findFirst()
                .orElseGet(() -> getDefaultMethod(update));
    }

    private BotApiMethodMessage getDefaultMethod(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Cannot handle message")
                .build();
    }
}
