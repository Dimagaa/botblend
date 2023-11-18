package com.app.botblend.client.openai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record CompletionRequest(
        String model,
        List<OpenAiMessage> messages,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("max_tokens")
        Integer maxTokens,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer n,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Double temperature
) {

    public static Builder builder(String model, OpenAiMessage message) {
        return new Builder(model, message);
    }

    public static final class Builder {
        private final List<OpenAiMessage> messages;
        private final String model;
        private Integer maxTokens;
        private Integer choiceCount;
        private Double temperature;

        public Builder(String model, OpenAiMessage message) {
            Objects.requireNonNull(message, model);
            this.model = model;
            messages = List.of(message);
        }

        public Builder(String model, List<OpenAiMessage> messages) {
            Objects.requireNonNull(messages, model);
            this.model = model;
            this.messages = Collections.unmodifiableList(messages);
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder choiceCount(Integer choiceCount) {
            this.choiceCount = choiceCount;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public CompletionRequest build() {
            return new CompletionRequest(model, messages, maxTokens, choiceCount, temperature);
        }
    }
}
