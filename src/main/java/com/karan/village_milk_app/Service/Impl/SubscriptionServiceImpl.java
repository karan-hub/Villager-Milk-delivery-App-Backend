package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.CreateSubscriptionRequest;
import com.karan.village_milk_app.Dto.DeliveryScheduleDto;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Repositories.*;
import com.karan.village_milk_app.Request.CreateCustomSubscriptionRequest;
import com.karan.village_milk_app.Response.SubscriptionResponse;
import com.karan.village_milk_app.Security.SecurityUtils;
import com.karan.village_milk_app.Service.SubscriptionService;
import com.karan.village_milk_app.healpers.SubscriptionEventHelper;
import com.karan.village_milk_app.model.*;
import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.PlanType;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionsRepository subscriptionRepo;
    private final SubscriptionEventsRepository eventRepo;
    private final SubscriptionPlanRepository planRepo;
    private  final ProductRepository  productRepository;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private  final  SubscriptionDeliveryRuleRepo  deliveryRuleRepo;
    private  final SubscriptionEventHelper subHelper;

    /* ================= CREATE SUBSCRIPTION ================= */

    @Override
    @Transactional
    public SubscriptionResponse createSubscription(CreateSubscriptionRequest req) {

        UUID userId = SecurityUtils.getCurrentUserId();

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SubscriptionPlan plan = planRepo.findById(req.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        Product product = productRepository.findById(req.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );

        Subscriptions sub = new Subscriptions();
        sub.setUser(user);
        sub.setPlan(plan);
        sub.setProduct(product);
        sub.setStartDate(req.getStartDate());
        sub.setEndDate(req.getStartDate().plusDays(plan.getDurationDays()));
        sub.setStatus(SubscriptionStatus.PENDING_PAYMENT);
        sub.setDeliverySlot(req.getDeliverySlot());
        sub.setQuantity(req.getQuantity());
        sub.setPlanType(req.getPlanType());


        subHelper.validateSubscriptionType(sub);
        Subscriptions saved = subscriptionRepo.save(sub);

//        generateSubscriptionEvents(saved, req.getDeliverySlot());

        return modelMapper.map(saved, SubscriptionResponse.class);
    }

    /* ================= GET MY SUBSCRIPTIONS ================= */

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getMySubscriptions() {

        UUID userId = SecurityUtils.getCurrentUserId();

        return subscriptionRepo.findByUserId(userId)
                .stream()
                .map(s -> modelMapper.map(s, SubscriptionResponse.class))
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
    public List<SubscriptionResponse> getAllSubscriptions() {
        return subscriptionRepo.findAll()
                .stream()
                .map(s -> modelMapper.map(s, SubscriptionResponse.class))
                .toList();
    }

    @Transactional
    public SubscriptionResponse createCustomSubscription(
            CreateCustomSubscriptionRequest req,
            UUID userId
    ) {
        if (req.getDeliverySchedule().isEmpty()) {
            throw new IllegalArgumentException("Delivery schedule cannot be empty");
        }


        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Subscriptions sub = new Subscriptions();
        sub.setUser(user);
        sub.setProduct(product);
        sub.setPlan(null);
        sub.setPlanType(PlanType.CUSTUME);
        sub.setQuantity(null);
        sub.setStartDate(req.getStartDate());
        sub.setEndDate(req.getEndDate());
        sub.setDeliverySlot(req.getDeliverySlot());
        sub.setStatus(SubscriptionStatus.PENDING_PAYMENT);

        subHelper.validateSubscriptionType(sub);

        Subscriptions saved = subscriptionRepo.save(sub);

        // 🔹 Persist delivery rules
        for (DeliveryScheduleDto dto : req.getDeliverySchedule()) {

            SubscriptionDeliveryRule rule = new SubscriptionDeliveryRule();
            rule.setSubscription(saved);
            rule.setDayOfWeek(DayOfWeek.of(
                    dto.getDayOfWeek() == 0 ? 7 : dto.getDayOfWeek()
            ));
            rule.setUnits(dto.getUnits());

            deliveryRuleRepo.save(rule);
        }

//        generateSubscriptionEventsFromRules(saved);

        return modelMapper.map(saved ,SubscriptionResponse.class);
    }

}
