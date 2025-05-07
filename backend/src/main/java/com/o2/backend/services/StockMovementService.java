package com.o2.backend.services;

import com.o2.backend.dtos.StockMovementDTO;
import com.o2.backend.exceptions.InsufficientStockException;
import com.o2.backend.exceptions.ResourceNotFoundException;
import com.o2.backend.models.Product;
import com.o2.backend.models.StockMovement;
import com.o2.backend.models.enums.MovementType;
import com.o2.backend.repositories.ProductRepository;
import com.o2.backend.repositories.StockMovementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockMovementService {

    private StockMovementRepository movementRepository;

    private ProductRepository productRepository;

    public StockMovementDTO registerMovement(StockMovementDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (dto.getType() == MovementType.EXIT && product.getQuantity() < dto.getQuantity()) {
            throw new InsufficientStockException("Not enough stock for exit movement");
        }

        // Update product quantity
        int newQuantity = dto.getType() == MovementType.ENTRY
                ? product.getQuantity() + dto.getQuantity()
                : product.getQuantity() - dto.getQuantity();

        product.setQuantity(newQuantity);
        productRepository.save(product);

        // Save movement record
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setQuantity(dto.getQuantity());
        movement.setType(dto.getType());
        movementRepository.save(movement);

        dto.setProductId(product.getId());
        return dto;
    }

    public List<StockMovementDTO> getMovementsByProduct(Long productId) {
        return movementRepository.findByProductId(productId).stream().map(movement -> {
            StockMovementDTO dto = new StockMovementDTO();
            dto.setProductId(productId);
            dto.setQuantity(movement.getQuantity());
            dto.setType(movement.getType());
            return dto;
        }).collect(Collectors.toList());
    }
}

