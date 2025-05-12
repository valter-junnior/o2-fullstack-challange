package com.o2.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2.backend.dtos.ProductDTO;
import com.o2.backend.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Product Description");
        productDTO.setQuantity(10);
        productDTO.setUnitPrice(new BigDecimal("100.0"));
        productDTO.setCategory("Electronics");
        productDTO.setCreatedAt(LocalDateTime.now());
        productDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldReturnPaginatedProducts() throws Exception {
        List<ProductDTO> products = Collections.singletonList(productDTO);
        PageImpl<ProductDTO> page = new PageImpl<>(products, PageRequest.of(0, 10), products.size());
        when(productService.paginated(any(Pageable.class))).thenReturn(page);

        ResultActions result = mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Product"));
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        List<ProductDTO> products = Collections.singletonList(productDTO);
        when(productService.all()).thenReturn(products);

        ResultActions result = mockMvc.perform(get("/api/products/all")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        when(productService.create(ArgumentMatchers.any(ProductDTO.class))).thenReturn(productDTO);

        ResultActions result = mockMvc.perform(post("/api/products")
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Product Description"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        when(productService.update(eq(1L), ArgumentMatchers.any(ProductDTO.class))).thenReturn(productDTO);

        ResultActions result = mockMvc.perform(put("/api/products/{id}", 1L)
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Product Description"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        doNothing().when(productService).delete(1L);

        ResultActions result = mockMvc.perform(delete("/api/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
        verify(productService, times(1)).delete(1L);
    }
}
