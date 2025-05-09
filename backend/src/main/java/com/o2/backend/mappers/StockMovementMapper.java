package com.o2.backend.mappers;

import com.o2.backend.dtos.StockMovementDTO;
import com.o2.backend.models.StockMovement;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StockMovementMapper {
    private ProductMapper productMapper;

    public StockMovementDTO toDTO(StockMovement stockMovement) {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setId(stockMovement.getId());
        dto.setProductId(stockMovement.getProduct().getId());
        dto.setQuantity(stockMovement.getQuantity());
        dto.setType(stockMovement.getType());
        dto.setDate(stockMovement.getDate());
        dto.setProduct(productMapper.toDTO(stockMovement.getProduct()));
        return dto;
    }

    public StockMovement toModel(StockMovementDTO dto) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(dto.getQuantity());
        stockMovement.setType(dto.getType());
        return stockMovement;
    }
}
