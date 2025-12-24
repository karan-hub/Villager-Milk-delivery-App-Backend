package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.SubscriptionEventsRepository;
import com.karan.village_milk_app.Repositories.SubscriptionsRepository;
import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.Service.AdminSubscriptionService;
import com.karan.village_milk_app.model.DeliveryDto;
import com.karan.village_milk_app.model.SubscriptionEvents;
import com.karan.village_milk_app.model.Subscriptions;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

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
    public List<SubscriptionDto> getSubscriptions(
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

    private SubscriptionDto toDto(Subscriptions s) {
        SubscriptionDto dto = modelMapper.map(s, SubscriptionDto.class);
        dto.setUserId(s.getUser().getId());
        dto.setPlanId(s.getPlan().getId());
        return dto;
    }

    private DeliveryDto toDeliveryDto(SubscriptionEvents e) {
        DeliveryDto dto = modelMapper.map(e, DeliveryDto.class);
        dto.setSubscriptionId(e.getSubscription().getId());
        dto.setUserId(e.getSubscription().getUser().getId());
        return dto;
    }
}
