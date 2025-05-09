package com.o2.backend.dtos;

import com.o2.backend.models.enums.MovementType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StockMovementDTO {
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private LocalDate date;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private MovementType type;

    private ProductDTO product;
}

