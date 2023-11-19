package com.app.botblend.exception;

public class TelegramBotApiException extends RuntimeException {
    public TelegramBotApiException(String message) {
        super(message);
    }

    public TelegramBotApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
