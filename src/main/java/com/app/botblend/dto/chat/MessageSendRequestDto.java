package com.app.botblend.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record MessageSendRequestDto(
        @NonNull
        @Schema(description = "Role of the sender",
                example = "admin",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String sender,

        @NonNull
        @Schema(description = "Message to send",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String content
) {
}
