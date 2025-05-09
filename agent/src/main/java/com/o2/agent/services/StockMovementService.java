package com.o2.agent.services;

import com.o2.agent.dtos.ActionConsultExitMovementDTO;
import com.o2.agent.dtos.ActionCreateMovementDTO;
import com.o2.agent.dtos.StockMovementDTO;
import com.o2.agent.dtos.TotalExitMovementsDTO;
import com.o2.agent.exceptions.InvalidResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StockMovementService {

    private final WebClient webClient;

    public StockMovementService(WebClient.Builder webClientBuilder, @Value("${chatbot.backend-url}") String backendUrl) {
        System.out.println("backendUrl: " + backendUrl);
        this.webClient = webClientBuilder.baseUrl(backendUrl).build();
    }

    public StockMovementDTO create(ActionCreateMovementDTO actionResponseDTO) {
        System.out.println("actionResponseDTO: " + actionResponseDTO.toString());

        StockMovementDTO response = webClient.post()
                .uri("/movements")
                .bodyValue(actionResponseDTO)
                .retrieve()
                .bodyToMono(StockMovementDTO.class)
                .block();

        if(response == null) {
            throw new InvalidResponseException("Erro ao cadastrar movimento.");
        }

        return response;
    }

    public TotalExitMovementsDTO consultExit(ActionConsultExitMovementDTO actionResponseDTO) {
        TotalExitMovementsDTO response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movements/total-exit")
                        .queryParam("startAt", actionResponseDTO.getStartAt())
                        .queryParam("endAt", actionResponseDTO.getEndAt())
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), res -> {
                    throw new InvalidResponseException("Erro ao consultar movimento de saída: " + res.statusCode());
                })
                .bodyToMono(TotalExitMovementsDTO.class)
                .block();

        if(response == null) {
            throw new InvalidResponseException("Erro ao consultar movimento de saída.");
        }

        return response;
    }
}
