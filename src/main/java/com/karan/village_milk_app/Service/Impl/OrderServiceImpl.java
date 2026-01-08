package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.OrderDto;
import com.karan.village_milk_app.Dto.OrderItemDto;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Repositories.OrderRepository;
import com.karan.village_milk_app.Repositories.ProductRepository;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Request.CreateOrderItemRequest;
import com.karan.village_milk_app.Request.CreateOrderRequest;
import com.karan.village_milk_app.Security.SecurityUtils;
import com.karan.village_milk_app.Service.OrderService;
import com.karan.village_milk_app.model.OrderItem;
import com.karan.village_milk_app.model.Orders;
import com.karan.village_milk_app.model.Product;
import com.karan.village_milk_app.model.Type.OrderStatus;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository ordersRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override

    public OrderDto placeOrder(CreateOrderRequest request) {

        UUID userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Orders order = new Orders();
        order.setUser(user);
        order.setDeliverySlot(request.getDeliverySlot());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice());

            BigDecimal itemTotal =
                    product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            item.setTotalPrice(itemTotal);

            order.getOrderItems().add(item);
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);

        Orders saved = ordersRepository.saveAndFlush(order);
        Orders persisted = ordersRepository.findById(saved.getId()).orElseThrow();

        OrderDto mapped = modelMapper.map(persisted, OrderDto.class);
        mapped.setOrderItems(
                persisted.getOrderItems().stream()
                        .map(item -> modelMapper.map(item, OrderItemDto.class))
                        .toList()
        );
        mapped.setOrderId(persisted.getId());

        return  mapped;
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getMyOrders() {

        UUID userId = SecurityUtils.getCurrentUserId();

        return ordersRepository.findByUserIdWithItemsOrderByCreatedAtDesc(userId)
                .stream()
                .map(o -> modelMapper.map(o, OrderDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(UUID orderId) {

        UUID userId = SecurityUtils.getCurrentUserId();

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not allowed to view this order");
        }

        return modelMapper.map(order, OrderDto.class);
    }
}
