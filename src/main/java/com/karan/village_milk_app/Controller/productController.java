package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Service.ProductService;
import com.karan.village_milk_app.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class productController {
    private  final ProductService productService;

    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(defaultValue = "0") int page  ,
            @RequestParam(defaultValue = "10") int size){
        return productService.getProducts(page, size);
    }
    }

