package com.o2.backend.controllers;

import com.o2.backend.services.ReportService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportController {

    private ReportService reportService;

    @GetMapping("/stock")
    public ResponseEntity<Map<String, Object>> getStockReport() {
        Map<String, Object> stockReport = reportService.getStockReport();
        return ResponseEntity.ok(stockReport);
    }

    @GetMapping("/movements")
    public ResponseEntity<List<Map<String, Object>>> getMovementsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Map<String, Object>> movementsReport = reportService.getMovementsByPeriod(startDate, endDate);
        return ResponseEntity.ok(movementsReport);
    }

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Object> salesReport = reportService.getSalesByPeriod(startDate, endDate);
        return ResponseEntity.ok(salesReport);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<Map<String, Object>>> getTopProducts() {
        List<Map<String, Object>> topProducts = reportService.getTopProducts();
        return ResponseEntity.ok(topProducts);
    }
}
