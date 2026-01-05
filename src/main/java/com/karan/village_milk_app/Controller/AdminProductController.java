package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Dto.ProductDto;
import com.karan.village_milk_app.Service.AdminService;
import com.karan.village_milk_app.Service.Impl.AdminServiceImpl;
import com.karan.village_milk_app.Service.Impl.ProductServiceImpl;

import com.karan.village_milk_app.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final AdminService adminService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody @Valid ProductDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminService.createProduct(dto));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductDto dto) throws BadRequestException {
        return ResponseEntity.ok(adminService.updateProduct(productId, dto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) throws BadRequestException {
        adminService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(productService.getProducts(page, size));
    }
}

