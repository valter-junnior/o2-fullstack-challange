package com.o2.backend.controllers;

import com.o2.backend.dtos.StockMovementDTO;
import com.o2.backend.services.StockMovementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movements")
@AllArgsConstructor
public class StockMovementController {

    private StockMovementService movementService;

    @GetMapping
    public ResponseEntity<Page<StockMovementDTO>> paginated(
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endAt) {
        return ResponseEntity.ok(movementService.paginated(pageable, startAt, endAt));
    }

    @PostMapping
    public ResponseEntity<StockMovementDTO> registerMovement(@Valid @RequestBody StockMovementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.registerMovement(dto));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovementDTO>> getMovementsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(movementService.getMovementsByProduct(productId));
    }

}

