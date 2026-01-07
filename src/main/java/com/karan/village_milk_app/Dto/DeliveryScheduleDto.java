package com.karan.village_milk_app.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeliveryScheduleDto {

    @Min(0) @Max(6)
    private int dayOfWeek; // 0 = Sunday

    @Min(1)
    private int units;
}
