package com.o2.backend.config;

import com.o2.backend.models.Product;
import com.o2.backend.models.StockMovement;
import com.o2.backend.models.enums.MovementType;
import com.o2.backend.repositories.ProductRepository;
import com.o2.backend.repositories.StockMovementRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private ProductRepository productRepository;

    private StockMovementRepository stockMovementRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() > 0) {
            return;
        }

        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Product product = Product.builder()
                    .name("Produto " + (i + 1))
                    .description("Descrição do Produto " + (i + 1))
                    .quantity(Math.abs(new Random().nextInt() % 100))
                    .unitPrice(BigDecimal.valueOf(20.0))
                    .build();

            products.add(product);
        }


        productRepository.saveAll(products);

        List<StockMovement> movements = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Product product = products.get(new Random().nextInt(products.size()));
            int quantity = Math.abs(new Random().nextInt() % 100);
            MovementType type = new Random().nextBoolean() ? MovementType.ENTRY : MovementType.EXIT;

            StockMovement movement = StockMovement.builder()
                    .product(product)
                    .quantity(quantity)
                    .date(LocalDate.now().minusDays(new Random().nextInt(30)))
                    .type(type)
                    .build();

            movements.add(movement);

        }

        stockMovementRepository.saveAll(movements);

        System.out.println("Dados de exemplo inseridos com sucesso!");
    }
}
