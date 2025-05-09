package com.o2.agent.services;

import com.o2.agent.config.ChatBotProperties;
import com.o2.agent.dtos.ChatBotRequestDTO;
import com.o2.agent.dtos.ChatBotResponseDTO;
import com.o2.agent.utils.FileLoader;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatBotService {
    private final ChatBotProperties properties;
    private final RestTemplate restTemplate;
    private final FileLoader fileLoader;

    public String sendMessage(String message) {
        ChatBotRequestDTO.Message systemMessage = new ChatBotRequestDTO.Message("system", fileLoader.load(properties.getPromptPath()));
        ChatBotRequestDTO.Message userMessage = new ChatBotRequestDTO.Message("user", message);
        ChatBotRequestDTO chatRequest = new ChatBotRequestDTO("llama-3.1-8b-instant", List.of(systemMessage, userMessage), 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());

        HttpEntity<ChatBotRequestDTO> request = new HttpEntity<>(chatRequest, headers);
        ResponseEntity<ChatBotResponseDTO> response = restTemplate.exchange(properties.getApiUrl(), HttpMethod.POST, request, ChatBotResponseDTO.class);

        ChatBotResponseDTO responseBody = response.getBody();
        if (responseBody != null && responseBody.getChoices() != null && !responseBody.getChoices().isEmpty()) {
            return responseBody.getChoices().getFirst().getMessage().getContent();
        }

        return "no_action";
    }
}
