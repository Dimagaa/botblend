package com.app.botblend.client.openai.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

public record OpenAiMessage(
        String role,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String name,
        String content
) {

    public static Builder builder(Role role, String content) {
        return new Builder(role, content);
    }

    public static class Builder {
        private final String role;
        private final String content;
        private String name;

        public Builder(Role role, String content) {
            Objects.requireNonNull(role, content);
            this.role = role.name().toLowerCase();
            this.content = content;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public OpenAiMessage build() {
            return new OpenAiMessage(role, name, content);
        }
    }

    public enum Role {
        SYSTEM,
        USER,
        ASSISTANT
    }
}
