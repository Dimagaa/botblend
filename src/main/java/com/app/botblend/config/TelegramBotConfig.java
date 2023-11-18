package com.app.botblend.config;

import com.app.botblend.client.telegram.TelegramBotClient;
import com.app.botblend.exception.TelegramBotException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@Configuration
public class TelegramBotConfig {
    private final TelegramBotClient telegramBotClient;

    public void registerBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotClient);
        } catch (TelegramApiException e) {
            throw new TelegramBotException("Unable register a new telegram bot", e);
        }
    }
}
