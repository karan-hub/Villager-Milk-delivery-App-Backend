package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.SubscriptionEventsRepository;
import com.karan.village_milk_app.Repositories.SubscriptionsRepository;
import com.karan.village_milk_app.Response.SubscriptionResponse;
import com.karan.village_milk_app.Service.AdminSubscriptionService;
import com.karan.village_milk_app.model.*;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSubscriptionServiceImpl
        implements AdminSubscriptionService {

    private final SubscriptionsRepository subscriptionRepo;
    private final SubscriptionEventsRepository eventRepo;
    private final ModelMapper modelMapper;

    /* ================= SUBSCRIPTIONS ================= */


    @Override
    public List<SubscriptionResponse> getSubscriptions(
            SubscriptionStatus status
    ) {

        List<Subscriptions> subs =
                (status == null)
                        ? subscriptionRepo.findAll()
                        : subscriptionRepo.findAllByStatus(status);

        return subs.stream()
                .map(this::toDto)
                .toList();
    }

    /* ================= DELIVERIES ================= */

    @Override
    public List<DeliveryDto> getDeliveries(
            LocalDate date,
            EventStatus status
    ) {
        LocalDate targetDate =
                (date == null)
                        ? LocalDate.now(ZoneId.of("Asia/Kolkata"))
                        : date;

        List<SubscriptionEvents> events =
                (status == null)
                        ? eventRepo.findByDeliveryDate(targetDate)
                        : eventRepo.findByDeliveryDateAndStatus(
                        targetDate,
                        status
                );

        return events.stream()
                .map(this::toDeliveryDto)
                .toList();
    }

    /* ================= SAFE MAPPERS ================= */

    private SubscriptionResponse toDto(Subscriptions s) {

        SubscriptionResponse dto = new SubscriptionResponse();

        // Subscription basic info
        dto.setSubscriptionId(s.getId());
        dto.setUnits(s.getPlan().getUnits());
        dto.setStartDate(s.getStartDate());
        dto.setEndDate(s.getEndDate());
        dto.setDeliverySlot(dto.getDeliverySlot());
        dto.setStatus(s.getStatus());
        dto.setCreatedAt(s.getCreatedAt());

        // User info
        if (s.getUser() != null) {
            dto.setUserId(s.getUser().getId());
        }

        // Plan + Product info
        if (s.getPlan() != null) {
            dto.setPlanId(s.getPlan().getId());
            dto.setPlanTitle(s.getPlan().getTitle());

            if (s.getPlan().getProduct() != null) {
                dto.setProductName(s.getPlan().getProduct().getName());
            }
        }

        return dto;
    }


    private DeliveryDto toDeliveryDto(SubscriptionEvents e) {

        DeliveryDto dto = new DeliveryDto();

        // Event info
        dto.setEventId(e.getId());
        dto.setQuantity(e.getDeliveredQuantity());
        dto.setDeliverySlot(e.getDeliverySlot());
        dto.setDeliveryDate(e.getDeliveryDate());
        dto.setStatus(e.getStatus());

        Subscriptions subscription = e.getSubscription();
        if (subscription == null) {
            return dto;
        }

        dto.setSubscriptionId(subscription.getId());

        // User info
        User user = subscription.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setUserName(user.getUsername());
        }

        // Product info
        if (subscription.getPlan() != null &&
                subscription.getPlan().getProduct() != null) {

            dto.setProductName(
                    subscription.getPlan().getProduct().getName()
            );
        }

        return dto;
    }

}
