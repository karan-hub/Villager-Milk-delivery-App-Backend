package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.ProductDto;

import java.util.UUID;

public interface AdminService {
    ProductDto  createProduct(ProductDto product);
    ProductDto  updateProduct( UUID  productId,ProductDto product);
    void deleteProduct(UUID productId);
}
