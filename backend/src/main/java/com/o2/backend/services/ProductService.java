package com.o2.backend.services;

import com.o2.backend.exceptions.ResourceNotFoundException;
import com.o2.backend.models.Product;
import com.o2.backend.repositories.ProductRepository;
import com.o2.backend.dtos.ProductDTO;
import com.o2.backend.mappers.ProductMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDTO> all() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public Page<ProductDTO> paginated(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    public BigDecimal getTotalPrice() {
        return productRepository.findAll().stream()
                .map(product -> product.getUnitPrice().multiply(new BigDecimal(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getTotalQuantity() {
        return productRepository.findAll().stream()
                .map(Product::getQuantity)
                .reduce(0, Integer::sum);
    }

    public ProductDTO create(ProductDTO productDto) {
        Product product = productMapper.toModel(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO update(Long id, ProductDTO productDto) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }

        Product product = productMapper.toModel(productDto);
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }
}

