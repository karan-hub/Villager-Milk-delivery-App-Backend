package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.ProductDto;
import com.karan.village_milk_app.Dto.SubscriptionEventDto;
import com.karan.village_milk_app.Response.SubscriptionResponse;
import com.karan.village_milk_app.model.DeliveryDto;
import com.karan.village_milk_app.model.SubscriptionEvents;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.UUID;

public interface AdminService {


    ProductDto createProduct(ProductDto product);
    ProductDto updateProduct(UUID productId, ProductDto product) throws BadRequestException;
    void deleteProduct(UUID productId) throws BadRequestException;

    List<SubscriptionResponse> getAllSubscriptions();

    List<SubscriptionEventDto> getTodayDeliveries();
    void markDelivered(UUID eventId);

    void markMissed(UUID eventId);
}
