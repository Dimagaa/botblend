package com.app.botblend.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record MessageDto(
        @Schema(description = "Unique identifier for the content",
                example = "1")
        Long id,

        @Schema(description = "The date and time when the message was created.",
                example = "2023-11-21T12:34:56")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @Schema(description = "Sender of the content",
                example = "user")
        String sender,

        @Schema(description = "Content of the content",
                example = "Hello, world!")
        String content) {
}
