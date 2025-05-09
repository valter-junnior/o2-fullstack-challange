package com.o2.agent.services;

import com.o2.agent.dtos.ChatBotRequestDTO;
import com.o2.agent.dtos.ChatBotResponseDTO;
import com.o2.agent.utils.FileLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatBotService {
    private final String pathConfigPrompt = "config/prompt.txt";
    private final String apiKey = "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";
    private final String apiUrl = "https://api.groq.com/openai/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();
    private final FileLoader fileLoader = new FileLoader();

    public String sendMessage(String message) {
        ChatBotRequestDTO.Message systemMessage = new ChatBotRequestDTO.Message("system", fileLoader.load(pathConfigPrompt));
        ChatBotRequestDTO.Message userMessage = new ChatBotRequestDTO.Message("user", message);
        ChatBotRequestDTO chatRequest = new ChatBotRequestDTO("llama-3.1-8b-instant", List.of(systemMessage, userMessage), 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatBotRequestDTO> request = new HttpEntity<>(chatRequest, headers);
        ResponseEntity<ChatBotResponseDTO> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, ChatBotResponseDTO.class);

        ChatBotResponseDTO responseBody = response.getBody();
        if (responseBody != null && responseBody.getChoices() != null && !responseBody.getChoices().isEmpty()) {
            return responseBody.getChoices().getFirst().getMessage().getContent();
        }

        return "no_action";
    }
}
