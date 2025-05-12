package com.o2.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2.backend.dtos.StockMovementDTO;
import com.o2.backend.dtos.TotalExitMovementsDTO;
import com.o2.backend.models.enums.MovementType;
import com.o2.backend.services.StockMovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockMovementController.class)
@ActiveProfiles("test")
public class StockMovementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockMovementService movementService;

    @Autowired
    private ObjectMapper objectMapper;

    private StockMovementDTO stockMovementDTO;
    private TotalExitMovementsDTO totalExitMovementsDTO;

    @BeforeEach
    void setUp() {
        stockMovementDTO = new StockMovementDTO(1L, 1L, LocalDate.of(2025, 5, 12), 10, MovementType.EXIT, null);
        totalExitMovementsDTO = new TotalExitMovementsDTO(100L, new BigDecimal("1000.00"));
    }

    @Test
    void shouldReturnPaginatedMovements() throws Exception {
        // Simular dados paginados
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<StockMovementDTO> page = new PageImpl<>(Collections.singletonList(stockMovementDTO), pageable, 1);
        when(movementService.paginated(any(Pageable.class), any(), any())).thenReturn(page);

        // Realizar a requisição GET
        ResultActions result = mockMvc.perform(get("/api/movements")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

        // Verificar o status e conteúdo da resposta
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].quantity").value(10));
    }

    @Test
    void shouldRegisterMovement() throws Exception {
        // Simular o serviço de registro
        when(movementService.registerMovement(any(StockMovementDTO.class))).thenReturn(stockMovementDTO);

        // Realizar a requisição POST
        ResultActions result = mockMvc.perform(post("/api/movements")
                .content(objectMapper.writeValueAsString(stockMovementDTO))
                .contentType(MediaType.APPLICATION_JSON));

        // Verificar o status e conteúdo da resposta
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void shouldReturnMovementsByProduct() throws Exception {
        // Simular movimentos para um produto específico
        when(movementService.getMovementsByProduct(1L)).thenReturn(Collections.singletonList(stockMovementDTO));

        // Realizar a requisição GET
        ResultActions result = mockMvc.perform(get("/api/movements/product/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // Verificar o status e conteúdo da resposta
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @Test
    void shouldReturnTotalExitMovements() throws Exception {
        // Simular o serviço de total de saídas
        when(movementService.getTotalExitMovements(any(), any())).thenReturn(totalExitMovementsDTO);

        // Realizar a requisição GET
        ResultActions result = mockMvc.perform(get("/api/movements/total-exit")
                .param("startAt", "2025-05-01")
                .param("endAt", "2025-05-12")
                .contentType(MediaType.APPLICATION_JSON));

        // Verificar o status e conteúdo da resposta
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityExitMovements").value(100))
                .andExpect(jsonPath("$.priceExitMovements").value(1000.00));
    }
}
