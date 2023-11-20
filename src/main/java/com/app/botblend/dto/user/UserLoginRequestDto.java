package com.app.botblend.dto.user;

import com.app.botblend.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginRequestDto(
        @Email
        @NotBlank
        @Schema(example = "user@example.com")
        String email,

        @Pattern(regexp = ValidationConstants.PASSWORD_REGEXP,
                message = ValidationConstants.INVALID_PASSWORD_MESSAGE)
        @NotBlank
        @Schema(example = "Pwd12345")
        String password
) {
}
