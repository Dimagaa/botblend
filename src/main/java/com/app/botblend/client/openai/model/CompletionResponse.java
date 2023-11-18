package com.app.botblend.client.openai.model;

import java.util.List;

public record CompletionResponse(String id, List<OpenAiChoice> choices) {
}
