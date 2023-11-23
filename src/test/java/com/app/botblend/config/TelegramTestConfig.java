package com.app.botblend.config;

import com.app.botblend.client.telegram.TelegramBotClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TelegramTestConfig {
    @Bean
    @Primary
    public TelegramBotClient telegramBotClient() {
        return Mockito.mock(TelegramBotClient.class);
    }
}
