package com.karan.village_milk_app.Config;

import com.karan.village_milk_app.Dto.ProductDto;
import com.karan.village_milk_app.Dto.SubscriptionEventDto;
import com.karan.village_milk_app.Dto.SubscriptionPlanDto;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.healpers.OtpGenerator;
import com.karan.village_milk_app.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper mapper = new ModelMapper();

        // ===================== GLOBAL CONFIG =====================
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // ===================== USER =====================

        mapper.typeMap(UserDTO.class, User.class)
                .addMappings(m -> m.skip(User::setPassword));

        mapper.typeMap(User.class, UserDTO.class)
                .addMappings(m -> m.skip(UserDTO::setPassword));

        // ===================== PRODUCT =====================

        mapper.typeMap(Product.class, ProductDto.class)
                .addMappings(m -> {
                    m.map(Product::getId, ProductDto::setId);
                    m.map(Product::getName, ProductDto::setName);
                });

        // ===================== SUBSCRIPTION PLAN =====================

        mapper.typeMap(SubscriptionPlan.class, SubscriptionPlanDto.class)
                .addMappings(m -> {
                    m.map(SubscriptionPlan::getId,
                            SubscriptionPlanDto::setId);

                    m.map(src -> src.getProduct().getId(),
                            SubscriptionPlanDto::setProductId);

                    m.map(src -> src.getProduct().getName(),
                            SubscriptionPlanDto::setProductName);
                });

        // ===================== SUBSCRIPTIONS =====================

        mapper.typeMap(Subscriptions.class, SubscriptionDto.class)
                .addMappings(m -> {

                    // ID mapping
                    m.map(Subscriptions::getId,
                            SubscriptionDto::setSubscriptionId);

                    // Plan info
                    m.map(src -> src.getPlan().getTitle(),
                            SubscriptionDto::setPlanTitle);

                    m.map(src -> src.getPlan().getId(),
                            SubscriptionDto::setPlanId);

                    // Product info (via plan)
                    m.map(src -> src.getPlan().getProduct().getName(),
                            SubscriptionDto::setProductName);

                    // Quantity → units
                    m.map(Subscriptions::getQuantity,
                            SubscriptionDto::setUnits);

                    // User
                    m.map(src -> src.getUser().getId(),
                            SubscriptionDto::setUserId);
                });

        // ===================== SUBSCRIPTION EVENTS =====================

        mapper.typeMap(SubscriptionEvents.class, SubscriptionEventDto.class)
                .addMappings(m -> {
                    m.map(SubscriptionEvents::getId,
                            SubscriptionEventDto::setEventId);

                    m.map(SubscriptionEvents::getDeliveredQuantity,
                            SubscriptionEventDto::setQuantity);
                });

        // ===================== ORDERS =====================

        mapper.typeMap(OrderItem.class, OrderItemDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getProduct().getId(),
                            OrderItemDto::setProductId);
                });

        mapper.typeMap(Orders.class, OrderDto.class)
                .addMappings(m -> {
                    m.map(Orders::getId,
                            OrderDto::setOrderId);
                });

        return mapper;
    }



//       OTP Generator

    @Bean
    public OtpGenerator otpGenerator() {
        return new OtpGenerator();
    }


//       Time

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }


}
