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

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Page<ProductDTO> paginated(Pageable pageable) {
        return productRepository.findByDeletedAtIsNull(pageable)
                .map(productMapper::toDTO);
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

