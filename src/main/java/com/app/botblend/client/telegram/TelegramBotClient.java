package com.app.botblend.client.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBotClient extends TelegramLongPollingBot {
    private final String botUserName;

    public TelegramBotClient(
            @Value("${telegram.api.bot.key}") String token,
            @Value("${telegram.api.bot.name}") String botUserName
    ) {
        super(token);
        this.botUserName = botUserName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        System.out.println(message.getText());
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
