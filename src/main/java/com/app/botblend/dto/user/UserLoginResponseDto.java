package com.app.botblend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginResponseDto(
        @Schema(description = "A secure authentication token generated upon successful user login.",
                example = "doNotShareYourApiKeyWithOthersOrExposeItInTheClientSideCode")
        String token
) {
}
