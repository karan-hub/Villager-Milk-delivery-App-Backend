package com.karan.village_milk_app.Payments;

import com.karan.village_milk_app.Response.CashfreeOrderResponse;
import com.karan.village_milk_app.Response.CashfreeVerifyResponse;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CashfreeClient {

    @Value("${cashfree.app-id}")
    private String appId;

    @Value("${cashfree.secret-key}")
    private String secretKey;

    @Value("${cashfree.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public CashfreeOrderResponse cashfreeCreateOrder(
            String orderId,
            BigDecimal amount,
            User user
    ) {
        String email = user.getName()+user.getPhone()+"@gmail.com";

        Map<String, Object> payload = Map.of(
                "order_id", orderId,
                "order_amount", amount,
                "order_currency", "INR",
                "customer_details", Map.of(
                        "customer_id", user.getId().toString(),
                        "customer_email", email,
                        "customer_phone", user.getPhone()
                )
        );

        HttpHeaders headers = headers();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> res = restTemplate.postForEntity(
                baseUrl + "/pg/orders",
                entity,
                Map.class
        );

        return new CashfreeOrderResponse(
                (String) res.getBody().get("payment_session_id")
        );
    }

    public CashfreeVerifyResponse verifyOrder(String orderId) {

        HttpEntity<Void> entity = new HttpEntity<>(headers());

        ResponseEntity<Map> res = restTemplate.exchange(
                baseUrl + "/pg/orders/" + orderId,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map body = res.getBody();

        return new CashfreeVerifyResponse(
                (String) body.get("order_status"),
                new BigDecimal(body.get("order_amount").toString()),
                extractPaymentId(body)
        );
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.set("x-client-id", appId);
        h.set("x-client-secret", secretKey);
        h.set("x-api-version", "2023-08-01");
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    private String extractPaymentId(Map body) {
        List<Map> payments = (List<Map>) body.get("payments");
        return payments != null && !payments.isEmpty()
                ? payments.get(0).get("cf_payment_id").toString()
                : null;
    }
}
