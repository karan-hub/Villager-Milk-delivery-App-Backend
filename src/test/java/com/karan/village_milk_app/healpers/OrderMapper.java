package com.karan.village_milk_app.healpers;

import com.karan.village_milk_app.Dto.OrderDto;
import com.karan.village_milk_app.model.OrderItem;
import com.karan.village_milk_app.model.OrderItemDto;
import com.karan.village_milk_app.model.Orders;
import org.springframework.web.bind.annotation.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    OrderDto toDto(Orders order);

    @Mapping(source = "product.name", target = "productName")
    OrderItemDto toDto(OrderItem item);
}
