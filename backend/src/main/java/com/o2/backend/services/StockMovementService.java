package com.o2.backend.services;

import com.o2.backend.dtos.ExitPerPeriodDTO;
import com.o2.backend.dtos.StockMovementDTO;
import com.o2.backend.dtos.TopProductExitDTO;
import com.o2.backend.dtos.TotalExitMovementsDTO;
import com.o2.backend.exceptions.InsufficientStockException;
import com.o2.backend.exceptions.ResourceNotFoundException;
import com.o2.backend.mappers.ProductMapper;
import com.o2.backend.mappers.StockMovementMapper;
import com.o2.backend.models.Product;
import com.o2.backend.models.StockMovement;
import com.o2.backend.models.enums.MovementType;
import com.o2.backend.repositories.ProductRepository;
import com.o2.backend.repositories.StockMovementRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockMovementService {

    private StockMovementRepository movementRepository;
    private ProductRepository productRepository;
    private StockMovementMapper stockMovementMapper;
    private ProductMapper productMapper;

    public Page<StockMovementDTO> paginated(Pageable pageable, LocalDate startAt, LocalDate endAt) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        }

        return movementRepository.findByDateBetween(startAt, endAt, pageable)
                .map(stockMovementMapper::toDTO);
    }

    public StockMovementDTO registerMovement(StockMovementDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (dto.getType() == MovementType.EXIT && product.getQuantity() < dto.getQuantity()) {
            throw new InsufficientStockException("Not enough stock for exit movement");
        }

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
        movement.setDate(dto.getDate());
        movementRepository.save(movement);

        dto.setId(movement.getId());
        dto.setProductId(product.getId());
        dto.setProduct(productMapper.toDTO(product));
        return dto;
    }

    public Integer getTotalQuantityExit() {
        return movementRepository.findByType(MovementType.EXIT).stream()
                .map(StockMovement::getQuantity)
                .reduce(0, Integer::sum);
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

    public List<TopProductExitDTO> getTopProducts() {
        List<TopProductExitDTO> topProductExitDTOList = movementRepository.findTopProducts();

        return topProductExitDTOList.subList(0, Math.min(topProductExitDTOList.size(), 5));
    }

    public List<ExitPerPeriodDTO> getExitPerPeriod(LocalDate startDate, LocalDate endDate) {
        return movementRepository.findExitPerPeriod(startDate, endDate);
    }

    public TotalExitMovementsDTO getTotalExitMovements(LocalDate startAt, LocalDate endAt) {
        return movementRepository.findTotalExitMovements(startAt, endAt);
    }
}

