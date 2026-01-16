package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Payments.CashfreeClient;
import com.karan.village_milk_app.Repositories.OrderRepository;
import com.karan.village_milk_app.Repositories.PaymentsRepository;
import com.karan.village_milk_app.Repositories.SubscriptionEventsRepository;
import com.karan.village_milk_app.Repositories.SubscriptionsRepository;
import com.karan.village_milk_app.Request.InitiatePaymentRequest;
import com.karan.village_milk_app.Response.CashfreeOrderResponse;
import com.karan.village_milk_app.Response.CashfreeVerifyResponse;
import com.karan.village_milk_app.Response.PaymentResponse;
import com.karan.village_milk_app.Service.PaymentService;
import com.karan.village_milk_app.healpers.SubscriptionEventHelper;
import com.karan.village_milk_app.model.Orders;
import com.karan.village_milk_app.model.Payments;
import com.karan.village_milk_app.model.Subscriptions;
import com.karan.village_milk_app.model.Type.*;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentsRepository paymentRepo;
    private final OrderRepository orderRepo;
    private final SubscriptionsRepository subRepo;
    private final CashfreeClient cashfreeClient;
    private  final SubscriptionEventHelper subHelper;
    private  final SubscriptionEventsRepository eventRepo ;


    @Override
    @Transactional
    public PaymentResponse initiate(
            User user,
            InitiatePaymentRequest request
    ) {


        Orders order = null;
        Subscriptions subscription = null;

        // Validate amount
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }


        // Link payment to order or subscription with ownership validation
        if (request.orderType() == OrderType.BUY_ONCE) {
            order = orderRepo.findById(request.orderTypeId())
                    .orElseThrow();
            validateOwnership(order.getUser(), user);
            ;
        } else {
            subscription = subRepo.findById(request.orderTypeId())
                    .orElseThrow();
            validateOwnership(subscription.getUser(), user);
        }

        Payments payment = new Payments();
        payment.setUser(user);
        payment.setOrder(order);
        payment.setSubscription(subscription);
        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        String gatewayOrderId = "order_" + UUID.randomUUID();
        payment.setMyOrderId(gatewayOrderId);


        paymentRepo.save(payment);
        CashfreeOrderResponse cf = cashfreeClient.cashfreeCreateOrder(
                gatewayOrderId,
                request.amount(),
                user
        );


        return new PaymentResponse(
                payment.getId(),
                cf.paymentSessionId(),
                payment.getMyOrderId()
        );

    }



    @Override
    public PaymentStatus verify(UUID paymentId) {
        Payments payment = paymentRepo.findById(paymentId).orElseThrow();
        String gatewayOrderId =
                paymentRepo.findGatewayOrderId(paymentId);

        CashfreeVerifyResponse cf =
                cashfreeClient.verifyOrder(gatewayOrderId);

        updatePaymentStatus(paymentId, cf);
        return paymentRepo.findById(paymentId)
                .orElseThrow()
                .getPaymentStatus();
    }


    private void activate(Payments payment) {

        if (payment.getOrder() != null) {
            payment.getOrder().setStatus(OrderStatus.PROCESSING);
        }

        if (payment.getSubscription() != null) {
            Subscriptions sub = payment.getSubscription();
            sub.setStatus(SubscriptionStatus.ACTIVE);

            switch (sub.getPlanType()){
                case CUSTUME -> subHelper.generateSubscriptionEventsFromRules(sub);
                default -> subHelper.generateSubscriptionEvents(sub);
            }


        }
    }

    private void validateOwnership(User owner, User user) {
        if (!owner.getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
    }

    private void handlePaymentFailure(Payments payment) {


        if (payment.getOrder() != null) {
            Orders order = payment.getOrder();
            order.setStatus(OrderStatus.CANCELLED);
        }


        if (payment.getSubscription() != null) {
            Subscriptions sub = payment.getSubscription();
            sub.setStatus(SubscriptionStatus.PAYMENT_FAILED);
            eventRepo.deleteBySubscriptionId(sub.getId());
        }
    }

    @Transactional
    public void updatePaymentStatus(UUID paymentId, CashfreeVerifyResponse cf) {

        Payments payment = paymentRepo.findById(paymentId).orElseThrow();

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return;
        }


        boolean amountMatches =
                cf.amount().compareTo(payment.getAmount()) == 0;

        Set<String> successStates = Set.of("PAID", "SUCCESS");

        boolean statusOk = successStates.contains(cf.orderStatus());

         if (statusOk && amountMatches){
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaymentId(cf.cfPaymentId());
            activate(payment);

        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            handlePaymentFailure(payment);
        }

        paymentRepo.save(payment);
    }



}
