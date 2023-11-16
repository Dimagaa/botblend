package com.app.botblend.dto;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
