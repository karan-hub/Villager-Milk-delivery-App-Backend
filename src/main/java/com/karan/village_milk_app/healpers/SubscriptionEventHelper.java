package com.karan.village_milk_app.healpers;

import com.karan.village_milk_app.Repositories.SubscriptionEventsRepository;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.model.SubscriptionDeliveryRule;
import com.karan.village_milk_app.model.SubscriptionEvents;
import com.karan.village_milk_app.model.Subscriptions;
import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.PlanType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SubscriptionEventHelper {
    private final SubscriptionEventsRepository eventRepo;

    public void validateSubscriptionType(Subscriptions sub) {

        if (sub.getPlanType() == PlanType.CUSTUME) {
            if (sub.getPlan() != null) {
                throw new IllegalStateException("CUSTOM subscription must not have a plan");
            }
        }

        if (sub.getPlanType() == PlanType.PREDEFINED && sub.getPlan() == null) {
            throw new IllegalStateException("PLAN subscription must have a plan");
        }

    }

    public void generateSubscriptionEventsFromRules(Subscriptions sub) {


        Map<DayOfWeek, Integer> ruleMap =
                sub.getDeliveryRules()
                        .stream()
                        .collect(Collectors.toMap(
                                SubscriptionDeliveryRule::getDayOfWeek,
                                SubscriptionDeliveryRule::getUnits
                        ));

        LocalDate date = sub.getStartDate();

        while (!date.isAfter(sub.getEndDate())) {

            DayOfWeek dow = date.getDayOfWeek();

            Integer units = ruleMap.get(dow);
            if (units != null && units > 0) {

                SubscriptionEvents event = new SubscriptionEvents();
                event.setSubscription(sub);
                event.setDeliveryDate(date);
                event.setDeliverySlot(sub.getDeliverySlot());
                event.setDeliveredQuantity(units);
                event.setStatus(EventStatus.SCHEDULED);

                eventRepo.save(event);
            }

            date = date.plusDays(1);
        }
    }

    public void generateSubscriptionEvents(Subscriptions sub) {
        LocalDate date = sub.getStartDate();

        while (!date.isAfter(sub.getEndDate())) {

            SubscriptionEvents event = new SubscriptionEvents();
            DeliverySlot deliverySlot = sub.getDeliverySlot();
            event.setSubscription(sub);
            event.setDeliveryDate(date);
            event.setDeliverySlot(deliverySlot);
            if (sub.getPlanType() == PlanType.PREDEFINED) {
                event.setDeliveredQuantity(sub.getPlan().getUnits());
            } else {
                event.setDeliveredQuantity(sub.getQuantity());
            }
            event.setStatus(EventStatus.SCHEDULED);

            eventRepo.save(event);

            date = date.plusDays(1);
        }
    }
}
