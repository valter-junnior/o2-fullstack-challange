package com.o2.backend.repositories;

import com.o2.backend.models.StockMovement;
import com.o2.backend.models.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductId(Long productId);
    List<StockMovement> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<StockMovement> findByTypeAndDateBetween(MovementType type, LocalDate startDate, LocalDate endDate);
    @Query("SELECT m.product.id, m.product.name, SUM(m.quantity) " +
            "FROM StockMovement m GROUP BY m.product.id, m.product.name " +
            "ORDER BY SUM(m.quantity) DESC")
    List<Object[]> findTopProducts();

}

