package com.o2.agent.services;

import com.o2.agent.dtos.StockMovementDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReportMessageService {
    public String consultExitMovement(String startAt, String endAt, Long quantityExitMovements, BigDecimal priceExitMovements) {
        return String.format(
                "Aqui está o relatório solicitado de %s até %s:\n\n" +
                        "- Quantidade de Movimentos de Saída: %d\n" +
                        "- Valor Total de Movimentos de Saída: R$ %.2f",
                startAt, endAt,
                quantityExitMovements, priceExitMovements
        );
    }

    public String createMovement(StockMovementDTO stockMovementDTO) {
        return String.format(
                "Movimento de Estoque Criado com Sucesso:\n\n" +
                        "- ID do Movimento: %d\n" +
                        "- Produto: %s (#%d)\n" +
                        "- Quantidade: %d\n" +
                        "- Tipo de Movimento: %s\n" +
                        "- Data do Movimento: %s\n",
                stockMovementDTO.getId(),
                stockMovementDTO.getProduct().getName(),
                stockMovementDTO.getProduct().getId(),
                stockMovementDTO.getQuantity(),
                stockMovementDTO.getType().equals("EXIT") ? "Saída" : "Entrada",
                stockMovementDTO.getDate()
        );
    }
}
