package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Dto.OrderDto;
import com.karan.village_milk_app.Request.CreateOrderRequest;
import com.karan.village_milk_app.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(
            @RequestBody CreateOrderRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderDto>> myOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
