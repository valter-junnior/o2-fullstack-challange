package com.o2.backend.config;

import com.o2.backend.models.Product;
import com.o2.backend.models.StockMovement;
import com.o2.backend.models.enums.MovementType;
import com.o2.backend.repositories.ProductRepository;
import com.o2.backend.repositories.StockMovementRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private ProductRepository productRepository;

    private StockMovementRepository stockMovementRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() > 0) {
            return;
        }

        // Criando produtos utilizando o Builder
        Product product1 = Product.builder()
                .name("Produto A")
                .description("Descrição do Produto A")
                .quantity(100)
                .unitPrice(BigDecimal.valueOf(20.0))
                .build();

        Product product2 = Product.builder()
                .name("Produto B")
                .description("Descrição do Produto B")
                .quantity(200)
                .unitPrice(BigDecimal.valueOf(30.0))
                .build();

        Product product3 = Product.builder()
                .name("Produto C")
                .description("Descrição do Produto C")
                .quantity(150)
                .unitPrice(BigDecimal.valueOf(50.0))
                .build();


        // Salvando produtos no banco
        List<Product> products = Arrays.asList(product1, product2, product3);
        productRepository.saveAll(products);

        // Criando movimentações de estoque (entradas e saídas)
        StockMovement movement1 = StockMovement.builder()
                .product(product1)
                .quantity(10)
                .date(LocalDate.now())
                .type(MovementType.ENTRY)
                .build();

        StockMovement movement2 = StockMovement.builder()
                .product(product2)
                .quantity(15)
                .date(LocalDate.now().minusDays(2))
                .type(MovementType.EXIT)
                .build();

        StockMovement movement3 = StockMovement.builder()
                .product(product3)
                .quantity(5)
                .date(LocalDate.now().minusDays(1))
                .type(MovementType.ENTRY)
                .build();

        // Salvando movimentações no banco
        stockMovementRepository.saveAll(Arrays.asList(movement1, movement2, movement3));

        // Exibindo mensagem
        System.out.println("Dados de exemplo inseridos com sucesso!");
    }
}
