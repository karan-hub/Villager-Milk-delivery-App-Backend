package com.karan.village_milk_app.model;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.OrderStatus;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Setter
public class OrderDto {
    private UUID orderId;
    private List<OrderItemDto> items;
    private BigDecimal totalAmount;
    private DeliverySlot deliverySlot;
    private LocalDateTime deliveryDate;
    private OrderStatus status;

}
