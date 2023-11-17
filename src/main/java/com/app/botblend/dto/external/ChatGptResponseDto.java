package com.app.botblend.dto.external;

import java.util.List;

public record ChatGptResponseDto(
        String id,
        List<ChatGptChoiceDto> choices
) {
}
