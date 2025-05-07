package com.o2.backend.services;

import com.o2.backend.models.Product;
import com.o2.backend.models.StockMovement;
import com.o2.backend.models.enums.MovementType;
import com.o2.backend.repositories.ProductRepository;
import com.o2.backend.repositories.StockMovementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {

    private ProductRepository productRepository;
    private StockMovementRepository movementRepository;

    public Map<String, Object> getStockReport() {
        List<Product> products = productRepository.findAll();
        BigDecimal totalValue = BigDecimal.ZERO;

        List<Map<String, Object>> productList = new ArrayList<>();
        for (Product product : products) {
            BigDecimal productTotal = product.getUnitPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
            totalValue = totalValue.add(productTotal);

            productList.add(Map.of(
                    "name", product.getName(),
                    "quantity", product.getQuantity(),
                    "unitPrice", product.getUnitPrice(),
                    "totalValue", productTotal
            ));
        }

        return Map.of(
                "products", productList,
                "totalStockValue", totalValue
        );
    }

    public List<Map<String, Object>> getMovementsByPeriod(LocalDate startDate, LocalDate endDate) {
        List<StockMovement> movements = movementRepository.findByDateBetween(startDate, endDate);

        return movements.stream().map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productId", m.getProduct().getId());
                    map.put("productName", m.getProduct().getName());
                    map.put("date", m.getDate());
                    map.put("quantity", m.getQuantity());
                    map.put("type", m.getType());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        List<StockMovement> exits = movementRepository.findByTypeAndDateBetween(
                MovementType.EXIT, startDate, endDate
        );

        int totalQuantity = exits.stream().mapToInt(StockMovement::getQuantity).sum();
        BigDecimal totalValue = exits.stream()
                .map(m -> m.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(m.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of(
                "totalQuantity", totalQuantity,
                "totalValue", totalValue
        );
    }

    public List<Map<String, Object>> getTopProducts() {
        List<Object[]> results = movementRepository.findTopProducts();

        return results.stream().map(r -> Map.of(
                "productId", r[0],
                "productName", r[1],
                "totalMovements", r[2]
        )).collect(Collectors.toList());
    }
}
