package com.app.botblend.client.openai;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiRequestInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TEMPLATE = "Bearer %s";
    private final String openAiApiKey;

    public OpenAiRequestInterceptor(@Value("${openai.api.key}") String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(AUTHORIZATION_HEADER, TOKEN_TEMPLATE.formatted(openAiApiKey));
    }
}
