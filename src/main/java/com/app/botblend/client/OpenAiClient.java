package com.app.botblend.client;

import com.app.botblend.dto.external.ChatGptRequestDto;
import com.app.botblend.dto.external.ChatGptResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "chatGpt", url = "${openai.api.baseurl}")
public interface OpenAiClient {
    @PostMapping(path = "${openai.api.path.completions}")
    ChatGptResponseDto createChatCompletion(ChatGptRequestDto requestDto);
}
