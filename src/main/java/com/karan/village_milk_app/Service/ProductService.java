package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.model.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<Product> getProducts(int page , int size);
}
