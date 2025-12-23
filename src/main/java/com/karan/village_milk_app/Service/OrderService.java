package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.OrderDto;
import com.karan.village_milk_app.Request.CreateOrderRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto placeOrder(CreateOrderRequest request);

    List<OrderDto> getMyOrders();

    OrderDto getOrderById(UUID orderId);
}
