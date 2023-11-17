package com.app.botblend.dto.external;

import java.util.List;

public record ChatGprRequestDto(
        String model,
        List<ChatGptMessageDto> messages,
        Integer n,
        Double temperature
) {
}
