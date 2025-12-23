package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.ProductDto;
import com.karan.village_milk_app.Repositories.ProductRepository;
import com.karan.village_milk_app.Service.ProductService;
import com.karan.village_milk_app.healpers.UserHelper;
import com.karan.village_milk_app.model.Product;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    public Page<ProductDto> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductDto.class));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String id, ProductDto dto) {
        UUID productId = UserHelper.parseUUID(id);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

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

        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDto.class);
    }

    @Override
    public void deleteProduct(String id) {
        UUID productId = UserHelper.parseUUID(id);
        productRepository.deleteById(productId);
    }

    @Override
    public ProductDto getProductById(String id) {
        UUID productId = UserHelper.parseUUID(id);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductDto.class);
    }
}
