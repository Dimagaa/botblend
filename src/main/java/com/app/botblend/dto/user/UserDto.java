package com.app.botblend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserDto(
        @Schema(description = "Generated user identity", example = "1")
        Long id,

        @Schema(example = "user@example.com")
        String email,

        @Schema(example = "Bob")
        String firstName,

        @Schema(example = "Smith")
        String lastName
) {
}
