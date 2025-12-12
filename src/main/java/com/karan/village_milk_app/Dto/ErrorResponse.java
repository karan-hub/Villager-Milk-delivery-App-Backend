package com.karan.village_milk_app.Dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String massage,
        HttpStatus status

) {}
