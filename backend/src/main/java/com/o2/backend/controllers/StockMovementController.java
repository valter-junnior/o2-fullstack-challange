package com.o2.backend.controllers;

import com.o2.backend.dtos.StockMovementDTO;
import com.o2.backend.services.StockMovementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
@AllArgsConstructor
public class StockMovementController {

    private StockMovementService movementService;

    @PostMapping
    public ResponseEntity<StockMovementDTO> registerMovement(@Valid @RequestBody StockMovementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.registerMovement(dto));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovementDTO>> getMovementsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(movementService.getMovementsByProduct(productId));
    }
}

