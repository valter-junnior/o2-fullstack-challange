package com.o2.agent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StockMovementDTO {
    private Long id;
    private Long productId;
    private LocalDate date;
    private Integer quantity;
    private String type;
    private ProductDTO product;
}

