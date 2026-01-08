package com.karan.village_milk_app.Config;

import com.karan.village_milk_app.Dto.*;
import com.karan.village_milk_app.Response.DeliveryRuleResponse;
import com.karan.village_milk_app.Response.SubscriptionResponse;
import com.karan.village_milk_app.healpers.OtpGenerator;
import com.karan.village_milk_app.model.*;
import com.karan.village_milk_app.model.OrderDto;
import com.karan.village_milk_app.model.Type.PlanType;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import java.time.Clock;
import java.util.List;

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

        mapper.typeMap(Subscriptions.class, SubscriptionResponse.class)
                .setPostConverter(ctx -> {

                    Subscriptions src = ctx.getSource();
                    if (src == null) return ctx.getDestination();

                    SubscriptionResponse dest = ctx.getDestination();

                    // ===== Common fields =====
                    dest.setSubscriptionId(src.getId());
                    dest.setUserId(src.getUser().getId());


                    dest.setProductName(src.getProduct().getName());

                    dest.setStartDate(src.getStartDate());
                    dest.setEndDate(src.getEndDate());
                    dest.setDeliverySlot(src.getDeliverySlot());
                    dest.setStatus(src.getStatus());
                    dest.setCreatedAt(src.getCreatedAt());

                    // ===== PREDEFINED subscription =====
                    if (src.getPlanType() == PlanType.PREDEFINED) {

                        dest.setPlanId(src.getPlan().getId());
                        dest.setPlanTitle(src.getPlan().getTitle());

                        // ✅ Single fixed quantity
                        dest.setUnits(src.getQuantity());

                        // ❌ No per-day rules
                        dest.setDeliveryRules(null);
                    }

                    // ===== CUSTOM subscription =====
                    if (src.getPlanType() == PlanType.CUSTUME) {

                        // ❌ No plan info
                        dest.setPlanId(null);
                        dest.setPlanTitle(null);

                        // ❌ No single quantity
                        dest.setUnits(null);

                        // ✅ Per-day delivery rules
                        if (src.getDeliveryRules() != null) {
                            dest.setDeliveryRules(
                                    src.getDeliveryRules().stream()
                                            .map(rule -> {
                                                DeliveryRuleResponse r = new DeliveryRuleResponse();
                                                r.setDayOfWeek(rule.getDayOfWeek());
                                                r.setUnits(rule.getUnits());
                                                return r;
                                            })
                                            .toList()
                            );
                        } else {
                            dest.setDeliveryRules(List.of());
                        }
                    }

                    return dest;
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
        mapper.typeMap(Orders.class, OrderDto.class)
                .addMapping(Orders::getId, OrderDto::setOrderId);

        mapper.typeMap(OrderItem.class, OrderItemDto.class)
                .addMappings(m ->
                        m.map(src -> src.getProduct().getName(),
                                OrderItemDto::setProductName)
                );


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
