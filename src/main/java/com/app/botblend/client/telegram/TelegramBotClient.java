package com.app.botblend.client.telegram;

import com.app.botblend.exception.TelegramBotApiException;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotClient extends TelegramLongPollingBot {
    private final String botUserName;
    private final TelegramMessageHandlerManager messageHandlerManager;

    public TelegramBotClient(
            @Value("${telegram.api.bot.key}") String token,
            @Value("${telegram.api.bot.name}") String botUserName,
            TelegramMessageHandlerManager messageHandlerManager
    ) {
        super(token);
        this.botUserName = botUserName;
        this.messageHandlerManager = messageHandlerManager;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            setTypingAction(update.getMessage().getChatId());
            BotApiMethodMessage resultMethod = messageHandlerManager.handleUpdate(update);
            executeMethod(resultMethod);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    public <T extends Serializable> void executeMethod(BotApiMethod<T> method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            throw new TelegramBotApiException("Cannot execute Telegram Bot API method", e);
        }
    }

    private void setTypingAction(Long chatId) {
        executeMethod(SendChatAction.builder()
                .chatId(chatId)
                .action(ActionType.TYPING.toString())
                .build());
    }
}
