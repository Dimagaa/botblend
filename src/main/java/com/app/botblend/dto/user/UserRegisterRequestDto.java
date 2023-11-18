package com.app.botblend.dto.user;

import com.app.botblend.validation.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@FieldMatch(first = "password", second = "repeatPassword")
public record UserRegisterRequestDto(
        @Email
        @NotBlank
        @Schema(example = "user@example.com")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
                message = "Minimum eight characters, at least one uppercase"
                          + " letter, one lowercase letter and one number")
        @NotBlank
        @Schema(example = "Pwd12345")
        String password,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
                message = "Minimum eight characters, at least one uppercase"
                          + " letter, one lowercase letter and one number")
        @NotBlank
        @Schema(example = "Pwd12345")
        String repeatPassword,

        @NotBlank
        @Length(min = 3, max = 255)
        @Schema(example = "Bob")
        String firstName,

        @NotBlank
        @Length(min = 3, max = 255)
        @Schema(example = "Smith")
        String lastName
) {
}
