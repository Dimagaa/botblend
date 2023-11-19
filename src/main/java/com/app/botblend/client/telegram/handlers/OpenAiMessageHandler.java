package com.app.botblend.client.telegram.handlers;

import com.app.botblend.client.openai.OpenAiClient;
import com.app.botblend.client.openai.model.CompletionRequest;
import com.app.botblend.client.openai.model.OpenAiMessage;
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
    private static final int MAX_TOKEN = 100;
    private static final int DEFAULT_CHOICE = 0;

    private final OpenAiClient openAiClient;
    private final OpenAiMessagePersistenceService openAiMessagePersistenceService;

    @Value("${openai.api.default-model}")
    private String openaiModel;

    @Override
    public BotApiMethodMessage handle(Update update) {
        Message request = update.getMessage();

        OpenAiMessage openAiRequestMessage = OpenAiMessage.builder(
                OpenAiMessage.Role.USER,
                request.getText()
        ).build();
        openAiMessagePersistenceService.persist(request.getChatId(), openAiRequestMessage);

        OpenAiMessage openAiResponseMessage = requestOpenaiMessage(openAiRequestMessage);
        openAiMessagePersistenceService.persist(request.getChatId(), openAiResponseMessage);

        return SendMessage.builder()
                .chatId(request.getChatId())
                .text(openAiResponseMessage.content())
                .build();
    }

    @Override
    public boolean isSupport(Update update) {
        return update.hasMessage();
    }

    private OpenAiMessage requestOpenaiMessage(OpenAiMessage request) {
        CompletionRequest completionRequest = CompletionRequest.builder(
                        openaiModel, request)
                .maxTokens(MAX_TOKEN)
                .build();

        return openAiClient.createChatCompletion(completionRequest)
                .choices().get(DEFAULT_CHOICE)
                .message();
    }
}
