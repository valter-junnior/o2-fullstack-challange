package com.o2.agent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
