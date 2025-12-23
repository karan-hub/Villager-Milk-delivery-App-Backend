package com.karan.village_milk_app.Config;

import com.karan.village_milk_app.Dto.SubscriptionEventDto;
import com.karan.village_milk_app.Dto.SubscriptionPlanDto;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.healpers.OtpGenerator;
import com.karan.village_milk_app.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        /* ===================== USER ===================== */

        mapper.typeMap(UserDTO.class, User.class).addMappings(m -> {
            m.skip(User::setPassword);
        });

        mapper.typeMap(User.class, UserDTO.class).addMappings(m -> {
            m.skip(UserDTO::setPassword);
        });

        /* ===================== SUBSCRIPTION PLAN ===================== */

        mapper.typeMap(SubscriptionPlan.class, SubscriptionPlanDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getId(), SubscriptionPlanDto::setId);
                    m.map(src -> src.getProduct().getId(),
                            SubscriptionPlanDto::setProductId);
                    m.map(src -> src.getProduct().getName(),
                            SubscriptionPlanDto::setProductName);
                });

        /* ===================== ORDER ===================== */

        mapper.typeMap(OrderItem.class, OrderItemDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getProduct().getId(),
                            OrderItemDto::setProductId);
                });

        mapper.typeMap(Orders.class, OrderDto.class)
                .addMappings(m -> {
                    m.map(Orders::getId, OrderDto::setOrderId);
                });

        /* ===================== SUBSCRIPTION EVENTS ===================== */

        mapper.typeMap(SubscriptionEvents.class, SubscriptionEventDto.class)
                .addMappings(m -> {
                    m.map(SubscriptionEvents::getId,
                            SubscriptionEventDto::setEventId);
                    m.map(SubscriptionEvents::getDeliveredQuantity,
                            SubscriptionEventDto::setQuantity);
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
