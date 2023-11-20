package com.app.botblend.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

public record MessageSendRequestDto(
        @NotBlank
        @Schema(description = "Role of the sender",
                example = "admin",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String sender,

        @NotBlank
        @Schema(description = "Message to send",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String content
) {
}
