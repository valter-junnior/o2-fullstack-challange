package com.o2.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TotalExitMovementsDTO {
    private Long quantityExitMovements;
    private BigDecimal priceExitMovements;
}
