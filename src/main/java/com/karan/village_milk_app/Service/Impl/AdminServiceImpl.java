package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.ProductDto;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Repositories.ProductRepository;
import com.karan.village_milk_app.Service.AdminService;
import com.karan.village_milk_app.healpers.UserHelper;
import com.karan.village_milk_app.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        validateProduct(productDto);

        Product product = modelMapper.map(productDto, Product.class);
        Product saved = productRepository.save(product);

        log.info("Product created with id={}", saved.getId());

        return modelMapper.map(saved, ProductDto.class);
    }

    @Override
    public void deleteProduct(UUID productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found");
        }

        productRepository.deleteById(productId);
        log.warn("Product deleted with id={}", productId);
    }

    @Override
    public ProductDto updateProduct(UUID productId, ProductDto dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getType() != null) product.setType(dto.getType());
        if (dto.getUnit() != null) product.setUnit(dto.getUnit());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getInStock() != null) product.setInStock(dto.getInStock());
        if (dto.getImageUrl() != null) product.setImageUrl(dto.getImageUrl());
        if (dto.getTags() != null) product.setTags(dto.getTags());
        if (dto.getHighlights() != null) product.setHighlights(dto.getHighlights());
        if (dto.getBenefits() != null) product.setBenefits(dto.getBenefits());
        if (dto.getNutrition() != null) product.setNutrition(dto.getNutrition());

        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    private void validateProduct(ProductDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new IllegalArgumentException("Product price must be positive");
        }
    }
}

