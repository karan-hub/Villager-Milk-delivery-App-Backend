package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.CreateSubscriptionRequest;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Repositories.SubscriptionEventsRepository;
import com.karan.village_milk_app.Repositories.SubscriptionPlanRepository;
import com.karan.village_milk_app.Repositories.SubscriptionsRepository;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.Security.SecurityUtils;
import com.karan.village_milk_app.Service.SubscriptionService;
import com.karan.village_milk_app.model.SubscriptionEvents;
import com.karan.village_milk_app.model.SubscriptionPlan;
import com.karan.village_milk_app.model.Subscriptions;
import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionsRepository subscriptionRepo;
    private final SubscriptionEventsRepository eventRepo;
    private final SubscriptionPlanRepository planRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    /* ================= CREATE SUBSCRIPTION ================= */

    @Override
    @Transactional
    public SubscriptionDto createSubscription(CreateSubscriptionRequest req) {

        UUID userId = SecurityUtils.getCurrentUserId();

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SubscriptionPlan plan = planRepo.findById(req.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        Subscriptions sub = new Subscriptions();
        sub.setUser(user);
        sub.setPlan(plan);
        sub.setStartDate(req.getStartDate());
        sub.setEndDate(req.getStartDate().plusDays(plan.getDurationDays()));
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setDeliverySlot(req.getDeliverySlot());
        sub.setQuantity(req.getQuantity());
        sub.setPlanType(req.getPlanType());

        Subscriptions saved = subscriptionRepo.save(sub);

        generateSubscriptionEvents(saved, req.getDeliverySlot());

        return modelMapper.map(saved, SubscriptionDto.class);
    }

    /* ================= GET MY SUBSCRIPTIONS ================= */

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionDto> getMySubscriptions() {

        UUID userId = SecurityUtils.getCurrentUserId();

        return subscriptionRepo.findByUserId(userId)
                .stream()
                .map(s -> modelMapper.map(s, SubscriptionDto.class))
                .toList();
    }

    /* ================= SKIP DELIVERY ================= */

    @Override
    public void skipDelivery(UUID eventId) {

        SubscriptionEvents event = eventRepo.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (event.getStatus() != EventStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled delivery can be skipped");
        }

        event.setStatus(EventStatus.SKIPPED);
    }

    /* ================= CANCEL SUBSCRIPTION ================= */

    @Override
    public void cancelSubscription(UUID subscriptionId) {

        Subscriptions sub = subscriptionRepo.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        sub.setStatus(SubscriptionStatus.CANCELLED);

        List<SubscriptionEvents> futureEvents =
                eventRepo.findBySubscription_IdAndDeliveryDateAfter(
                        subscriptionId,
                        LocalDate.now()
                );

        for (SubscriptionEvents e : futureEvents) {
            e.setStatus(EventStatus.CANCELLED);
        }
    }

    /* ================= PAUSE SUBSCRIPTION ================= */

    @Override
    public void pauseSubscription(UUID subscriptionId) {

        Subscriptions sub = subscriptionRepo.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscription can be paused");
        }

        sub.setStatus(SubscriptionStatus.PAUSED);

        List<SubscriptionEvents> futureEvents =
                eventRepo.findBySubscription_IdAndDeliveryDateAfter(
                        subscriptionId,
                        LocalDate.now()
                );

        for (SubscriptionEvents e : futureEvents) {
            if (e.getStatus() == EventStatus.SCHEDULED) {
                e.setStatus(EventStatus.CANCELLED);
            }
        }
    }

    /* ================= RESUME SUBSCRIPTION ================= */

    @Override
    public void resumeSubscription(UUID subscriptionId) {

        Subscriptions sub = subscriptionRepo.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        if (sub.getStatus() != SubscriptionStatus.PAUSED) {
            throw new IllegalStateException("Only paused subscription can be resumed");
        }

        sub.setStatus(SubscriptionStatus.ACTIVE);

        List<SubscriptionEvents> futureEvents =
                eventRepo.findBySubscription_IdAndDeliveryDateAfter(
                        subscriptionId,
                        LocalDate.now()
                );

        for (SubscriptionEvents e : futureEvents) {
            if (e.getStatus() == EventStatus.CANCELLED) {
                e.setStatus(EventStatus.SCHEDULED);
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionRepo.findAll()
                .stream()
                .map(s -> modelMapper.map(s, SubscriptionDto.class))
                .toList();
    }

    /* ================= HELPER ================= */

    private void generateSubscriptionEvents(
            Subscriptions sub,
            DeliverySlot slot
    ) {
        LocalDate date = sub.getStartDate();

        while (!date.isAfter(sub.getEndDate())) {

            SubscriptionEvents event = new SubscriptionEvents();
            event.setSubscription(sub);
            event.setDeliveryDate(date);
            event.setDeliverySlot(slot);
            event.setDeliveredQuantity(sub.getPlan().getUnits());
            event.setStatus(EventStatus.SCHEDULED);

            eventRepo.save(event);

            date = date.plusDays(1);
        }
    }
}
