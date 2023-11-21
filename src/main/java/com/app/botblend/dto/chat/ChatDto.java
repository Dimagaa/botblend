package com.app.botblend.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatDto(
        @Schema(description = "Unique identifier for the chat",
                example = "1")
        Long id,

        @Schema(description = "External identifier for the chat",
                example = "1404")
        Long externalId
) {
}
