package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.ProductDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductDto> getProducts(int page, int size);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(String id, ProductDto productDto);
    void deleteProduct(String  id);
    ProductDto getProductById(String id);
}
