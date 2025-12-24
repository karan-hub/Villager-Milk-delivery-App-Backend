package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.OrderRepository;
import com.karan.village_milk_app.Repositories.SubscriptionEventsRepository;
import com.karan.village_milk_app.model.*;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionOrderScheduler {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionOrderScheduler.class);

    private final SubscriptionEventsRepository eventRepository;
    private final OrderRepository ordersRepository;

    @Transactional
    @Scheduled(cron = "0 0 5 * * *") // daily 5 AM
    public void generateDailyOrders() {

        LocalDate today = LocalDate.now();

        List<SubscriptionEvents> events =
                eventRepository.findByDeliveryDateAndStatus(
                        today,
                        EventStatus.SCHEDULED
                );

        if (events.isEmpty()) {
            log.info("No subscription events for {}", today);
            return;
        }

        for (SubscriptionEvents event : events) {


            if (ordersRepository.existsBySubscriptionEventId(event.getId())) {
                continue;
            }

            Subscriptions sub = event.getSubscription();
            SubscriptionPlan plan = sub.getPlan();

            Orders order = new Orders();
            order.setUser(sub.getUser());
            order.setDeliveryDate(today.atTime(6, 0));
            order.setDeliverySlot(event.getDeliverySlot());
            order.setStatus(OrderStatus.PENDING);
            order.setSubscriptionEventId(event.getId());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(plan.getProduct());
            item.setQuantity((long) plan.getUnits());
            item.setPrice(plan.getPrice());

            BigDecimal total =
                    plan.getPrice()
                            .multiply(BigDecimal.valueOf(plan.getUnits()));

            item.setTotalPrice(total);

            order.getOrderItems().add(item);
            order.setTotalAmount(total);

            ordersRepository.save(order);

            event.setStatus(EventStatus.DELIVERED);

            log.info("Order created for subscriptionEvent={}", event.getId());
        }
    }

    @Scheduled(cron = "0 0 10 * * *")
    @Transactional
    public void markMissedDeliveries() {

        LocalDate today = LocalDate.now();

        List<SubscriptionEvents> events =
                eventRepository.findByDeliveryDateAndStatus(
                        today,
                        EventStatus.SCHEDULED
                );

        for (SubscriptionEvents event : events) {
            event.setStatus(EventStatus.MISSED);
        }
    }

}
