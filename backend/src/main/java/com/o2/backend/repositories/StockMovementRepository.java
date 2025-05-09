package com.o2.backend.repositories;

import com.o2.backend.dtos.ExitPerPeriodDTO;
import com.o2.backend.dtos.TopProductExitDTO;
import com.o2.backend.dtos.TotalExitMovementsDTO;
import com.o2.backend.models.StockMovement;
import com.o2.backend.models.enums.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @EntityGraph(attributePaths = {"product"})
    @Query("SELECT m FROM StockMovement m WHERE " +
            "(:startAt IS NULL OR m.date >= :startAt) AND " +
            "(:endAt IS NULL OR m.date <= :endAt)")
    Page<StockMovement> findByDateBetween(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt,
            Pageable pageable
    );

    List<StockMovement> findByProductId(Long productId);

    @Query("SELECT new com.o2.backend.dtos.TopProductExitDTO(p.id, p.name, SUM(m.quantity), SUM(m.quantity * p.unitPrice)) " +
            "FROM StockMovement m " +
            "JOIN m.product p " +
            "WHERE m.type = com.o2.backend.models.enums.MovementType.EXIT " +
            "GROUP BY p.id " +
            "ORDER BY SUM(m.quantity) DESC")
    List<TopProductExitDTO> findTopProducts();

    List<StockMovement> findByType(MovementType type);

    @Query("SELECT new com.o2.backend.dtos.ExitPerPeriodDTO(DATE(m.date), SUM(m.quantity * p.unitPrice)) " +
            "FROM StockMovement m " +
            "JOIN m.product p " +
            "WHERE m.type = com.o2.backend.models.enums.MovementType.EXIT " +
            "AND m.date BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(m.date) " +
            "ORDER BY DATE(m.date)")
    List<ExitPerPeriodDTO> findExitPerPeriod(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.o2.backend.dtos.TotalExitMovementsDTO(SUM(m.quantity), SUM(m.quantity * p.unitPrice)) " +
            "FROM StockMovement m " +
            "JOIN m.product p " +
            "WHERE m.type = com.o2.backend.models.enums.MovementType.EXIT " +
            "AND m.date BETWEEN :startDate AND :endDate")
    TotalExitMovementsDTO findTotalExitMovements(LocalDate startDate, LocalDate endDate);

}

