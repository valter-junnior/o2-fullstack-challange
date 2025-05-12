package com.o2.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2.backend.dtos.ExitPerPeriodDTO;
import com.o2.backend.services.ProductService;
import com.o2.backend.services.StockMovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@ActiveProfiles("test")
public class ReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockMovementService stockMovementService;

    @MockitoBean
    private ProductService productService;

    private ExitPerPeriodDTO exitPerPeriodDTO;

    @BeforeEach
    void setUp() {
        exitPerPeriodDTO = new ExitPerPeriodDTO(java.sql.Date.valueOf("2025-05-12"), new BigDecimal("500.00"));
    }

    @Test
    void shouldReturnSalesPerPeriod() throws Exception {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate endDate = LocalDate.of(2025, 5, 12);

        when(stockMovementService.getExitPerPeriod(startDate, endDate)).thenReturn(Collections.singletonList(exitPerPeriodDTO));

        ResultActions result = mockMvc.perform(get("/api/reports/sales")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2025-05-12"))
                .andExpect(jsonPath("$[0].total").value(500.00));
    }
}
