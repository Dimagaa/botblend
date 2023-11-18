package com.app.botblend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginRequestDto(
        @Email
        @NotBlank
        @Schema(example = "user@example.com")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
                message = "Minimum eight characters, at least one uppercase"
                          + " letter, one lowercase letter and one number")
        @NotBlank
        @Schema(example = "Pwd12345")
        String password
) {
}
