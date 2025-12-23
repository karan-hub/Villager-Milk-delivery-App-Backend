package com.karan.village_milk_app.Dto;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID orderId;
    private UUID userId;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private BigDecimal totalAmount;
    private LocalDateTime deliveryDate;
    private DeliverySlot deliverySlot;
    private OrderStatus status = OrderStatus.PENDING;
    private Instant createdAt;
}
