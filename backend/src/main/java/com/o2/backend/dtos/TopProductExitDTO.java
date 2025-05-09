package com.o2.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TopProductExitDTO {
    private Long productId;
    private String productName;
    private Long totalQuantity;
    private BigDecimal totalPrice;
}
