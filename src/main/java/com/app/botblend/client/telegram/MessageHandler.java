package com.app.botblend.client.telegram;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    BotApiMethodMessage handle(Update update);

    boolean isSupport(Update update);
}
