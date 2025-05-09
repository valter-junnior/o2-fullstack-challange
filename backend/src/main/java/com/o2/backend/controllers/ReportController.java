package com.o2.backend.controllers;

import com.o2.backend.dtos.DashboardDTO;
import com.o2.backend.dtos.ExitPerPeriodDTO;
import com.o2.backend.services.ProductService;
import com.o2.backend.services.StockMovementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportController {

    private final ProductService productService;
    private final StockMovementService stockMovementService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> dashboard() {
        return ResponseEntity.ok(
                new DashboardDTO(
                        productService.getTotalPrice(),
                        productService.getTotalQuantity(),
                        stockMovementService.getTotalQuantityExit(),
                        stockMovementService.getTopProducts()
                )
        );
    }

    @GetMapping("/sales")
    public List<ExitPerPeriodDTO> getSalesPerPeriod(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return stockMovementService.getExitPerPeriod(startDate, endDate);
    }
}
