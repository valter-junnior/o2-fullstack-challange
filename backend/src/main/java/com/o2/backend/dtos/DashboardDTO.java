package com.o2.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class DashboardDTO {
    BigDecimal totalPriceProducts;
    Integer totalQuantityProducts;
    Integer totalQuantityExitMovements;
    List<TopProductExitDTO> topProductExits;
}
