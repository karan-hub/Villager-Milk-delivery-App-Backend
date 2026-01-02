package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.ProductDto;
import com.karan.village_milk_app.Dto.SubscriptionEventDto;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Repositories.SubscriptionEventsRepository;
import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.Service.AdminService;
import com.karan.village_milk_app.Service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.DeliveryDto;
import com.karan.village_milk_app.model.SubscriptionEvents;
import com.karan.village_milk_app.model.Type.EventStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final ProductService productService;
    private final SubscriptionEventsRepository eventRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        ProductDto created = productService.createProduct(productDto);
        log.info("Product created with id={}", created.getId());
        return created;
    }

    @Override
    public ProductDto updateProduct(UUID productId, ProductDto dto) {
        ProductDto updated = productService.updateProduct(productId.toString(), dto);
        log.info("Product updated with id={}", productId);
        return updated;
    }

    @Override
    public void deleteProduct(UUID productId) {
        productService.deleteProduct(productId.toString());
        log.warn("Product deleted with id={}", productId);
    }


    @Override

    public List<SubscriptionEventDto> getTodayDeliveries() {

        LocalDate today = LocalDate.now();

        return eventRepository.findByDeliveryDateAndStatus(today, EventStatus.SCHEDULED)
                .stream()
                .map(e -> modelMapper.map(e, SubscriptionEventDto.class))
                .toList();
    }

    @Override
    public void markDelivered(UUID eventId) {

        SubscriptionEvents event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        if (event.getStatus() != EventStatus.SCHEDULED) {
            throw new IllegalStateException(
                    "Only scheduled delivery can be marked delivered");
        }

        event.setStatus(EventStatus.DELIVERED);
        log.info("Delivery marked DELIVERED for event={}", eventId);
    }

    @Override
    public void markMissed(UUID eventId) {

        SubscriptionEvents event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        event.setStatus(EventStatus.MISSED);
        log.warn("Delivery marked MISSED for event={}", eventId);
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions() {
        // Implement as needed, perhaps return empty list or fetch from repository
        return List.of();
    }

}


