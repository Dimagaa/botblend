package com.app.botblend.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageDto(
        @Schema(description = "Unique identifier for the content",
                example = "1")
        Long id,

        @Schema(description = "Sender of the content",
                example = "user")
        String sender,

        @Schema(description = "Content of the content",
                example = "Hello, world!")
        String content) {
}
