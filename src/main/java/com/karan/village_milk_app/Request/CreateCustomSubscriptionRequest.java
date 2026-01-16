package com.karan.village_milk_app.Request;

import com.karan.village_milk_app.Dto.DeliveryScheduleDto;
import com.karan.village_milk_app.model.Type.DeliverySlot;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCustomSubscriptionRequest {

    @NotNull
    private UUID productId;
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private DeliverySlot deliverySlot;

    @NotEmpty
    private List<DeliveryScheduleDto> deliverySchedule;

}
