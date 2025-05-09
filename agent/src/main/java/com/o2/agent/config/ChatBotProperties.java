package com.o2.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "chatbot")
public class ChatBotProperties {
    private String promptPath;
    private String apiKey;
    private String apiUrl;
    private String backendUrl;
}