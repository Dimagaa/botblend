package com.app.botblend.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ChatDto(
        @Schema(description = "Unique identifier for the chat",
                example = "1")
        Long id,

        @Schema(description = "External identifier for the chat",
                example = "1404")
        Long externalId,

        @Schema(description = "List of messages in the chat's history")
        List<MessageDto> messages
) {
}
