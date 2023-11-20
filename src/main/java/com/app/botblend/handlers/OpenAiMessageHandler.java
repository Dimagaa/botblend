package com.app.botblend.handlers;

import com.app.botblend.client.openai.OpenAiClient;
import com.app.botblend.client.openai.model.CompletionRequest;
import com.app.botblend.client.openai.model.OpenAiMessage;
import com.app.botblend.client.telegram.MessageHandler;
import com.app.botblend.service.MessagePersistenceService;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class OpenAiMessageHandler implements MessageHandler {
    private static final String OVERLOADED_SERVER_MESSAGE = "Server overloaded. Please try again";
    private static final int DEFAULT_CHOICE = 0;

    private final OpenAiClient openAiClient;
    private final MessagePersistenceService<OpenAiMessage> messagePersistenceService;

    @Value("${openai.api.default-model}")
    private String openaiModel;

    @Override
    public BotApiMethodMessage handle(Update update) {
        Message request = update.getMessage();

        OpenAiMessage openAiRequestMessage = OpenAiMessage.builder(
                OpenAiMessage.Role.USER,
                request.getText()
        ).build();
        messagePersistenceService.persist(request.getChatId(), openAiRequestMessage);

        OpenAiMessage openAiResponseMessage = requestOpenaiMessage(openAiRequestMessage);
        messagePersistenceService.persist(request.getChatId(), openAiResponseMessage);

        return SendMessage.builder()
                .chatId(request.getChatId())
                .text(openAiResponseMessage.content())
                .build();
    }

    @Override
    public boolean isSupport(Update update) {
        return update.getMessage().hasText();
    }

    private OpenAiMessage requestOpenaiMessage(OpenAiMessage request) {
        CompletionRequest completionRequest = CompletionRequest.builder(
                        openaiModel, request)
                .build();

        try {
            return openAiClient.createChatCompletion(completionRequest)
                    .choices().get(DEFAULT_CHOICE)
                    .message();

        } catch (RetryableException e) {
            return OpenAiMessage.builder(
                            OpenAiMessage.Role.SYSTEM,
                            OVERLOADED_SERVER_MESSAGE)
                    .build();
        }
    }
}
