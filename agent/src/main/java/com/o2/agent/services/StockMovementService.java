package com.o2.agent.services;

import com.o2.agent.dtos.ActionConsultExitMovementDTO;
import com.o2.agent.dtos.ActionCreateMovementDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockMovementService {

    public String create(ActionCreateMovementDTO actionResponseDTO) {
        return "Movimento de estoque criado com sucesso.";
    }

    public String consultExit(ActionConsultExitMovementDTO actionResponseDTO) {
        return "Movimento de saída consultado com sucesso.";
    }
}

