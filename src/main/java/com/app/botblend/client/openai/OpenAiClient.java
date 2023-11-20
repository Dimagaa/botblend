package com.app.botblend.client.openai;

import com.app.botblend.client.openai.model.CompletionRequest;
import com.app.botblend.client.openai.model.CompletionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "open-ai", url = "${openai.api.baseurl}")
public interface OpenAiClient {

    @PostMapping(path = "${openai.api.path.completions}")
    CompletionResponse createChatCompletion(CompletionRequest requestDto);
}
