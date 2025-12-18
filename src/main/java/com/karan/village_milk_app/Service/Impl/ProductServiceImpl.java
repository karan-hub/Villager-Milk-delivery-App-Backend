package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.ProductRepository;
import com.karan.village_milk_app.Service.ProductService;
import com.karan.village_milk_app.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private   final  ProductRepository productRepository ;
    @Override
    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page , size , Sort.by("id").descending());
        return  productRepository.findAll(pageable);
    }
}
