package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.ProductDto;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductDto> getProducts(int page, int size);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(String id, ProductDto productDto) throws BadRequestException;
    void deleteProduct(String  id) throws BadRequestException;
    ProductDto getProductById(String id);
}
