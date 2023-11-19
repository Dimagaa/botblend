package com.app.botblend.config;

import com.app.botblend.client.telegram.TelegramBotClient;
import com.app.botblend.exception.TelegramBotApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@Configuration
@Profile("!test")
public class TelegramBotConfig {
    private final TelegramBotClient telegramBotClient;

    @EventListener(ApplicationReadyEvent.class)
    public void registerBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotClient);
        } catch (TelegramApiException e) {
            throw new TelegramBotApiException("Unable register a new telegram bot", e);
        }
    }
}
