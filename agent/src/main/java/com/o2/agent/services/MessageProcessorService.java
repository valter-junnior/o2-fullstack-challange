package com.o2.agent.services;

import com.o2.agent.dtos.*;
import com.o2.agent.enums.ActionEnum;
import com.o2.agent.exceptions.InvalidResponseException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageProcessorService {
    private final ObjectMapper objectMapper;
    private final StockMovementService stockMovementService;
    private final ReportMessageService reportMessageService;

    public MessageDTO process(String response) {
        try {
            return Optional.ofNullable(handleResponse(response))
                    .filter(ActionResponseDTO.class::isInstance)
                    .map(ActionResponseDTO.class::cast)
                    .map(this::handleAction)
                    .map(MessageDTO::new)
                    .orElseThrow(() -> new InvalidResponseException("Resposta inválida ou não reconhecida"));
        } catch (InvalidResponseException | IOException e) {
            return new MessageDTO(e.getMessage());
        }
    }

    private String handleAction(ActionResponseDTO actionResponseDTO) {
        switch (actionResponseDTO.getAction()) {
            case CREATE_STOCK_MOVEMENT -> {
                StockMovementDTO stockMovementDTO = stockMovementService.create((ActionCreateMovementDTO) actionResponseDTO);
                return reportMessageService.createMovement(stockMovementDTO);
            }
            case CONSULT_EXIT_STOCK_MOVEMENT -> {
                TotalExitMovementsDTO totalExitMovementsDTO = stockMovementService.consultExit((ActionConsultExitMovementDTO) actionResponseDTO);

                return reportMessageService.consultExitMovement(
                        ((ActionConsultExitMovementDTO) actionResponseDTO).getStartAt(),
                        ((ActionConsultExitMovementDTO) actionResponseDTO).getEndAt(),
                        totalExitMovementsDTO.getQuantityExitMovements(),
                        totalExitMovementsDTO.getPriceExitMovements()
                );
            }
            case MESSAGE_NOT_UNDERSTAND -> {
                ActionInvalidParameterDTO invalidParameterDTO = (ActionInvalidParameterDTO) actionResponseDTO;

                return invalidParameterDTO.getMessage();
            }
            default -> {
                return null;
            }
        }
    }

    public Object handleResponse(String jsonResponse) throws IOException {
        Map<String, Class<?>> actionMap = Map.of(
                ActionEnum.CONSULT_EXIT_STOCK_MOVEMENT.name(), ActionConsultExitMovementDTO.class,
                ActionEnum.CREATE_STOCK_MOVEMENT.name(), ActionCreateMovementDTO.class,
                ActionEnum.INVALID_PARAMETER.name(), ActionInvalidParameterDTO.class,
                ActionEnum.MESSAGE_NOT_UNDERSTAND.name(), Exception.class
        );

        for (Map.Entry<String, Class<?>> entry : actionMap.entrySet()) {
            if (jsonResponse.contains(entry.getKey())) {
                return objectMapper.readValue(jsonResponse, entry.getValue());
            }
        }

        return null;
    }
}

