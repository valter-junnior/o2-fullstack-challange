package com.o2.agent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
public class TotalExitMovementsDTO {
    private Long quantityExitMovements;
    private BigDecimal priceExitMovements;
}
