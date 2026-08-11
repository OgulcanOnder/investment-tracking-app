package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DtoInvestmentRequest(
        @DecimalMin(value = "0", inclusive = false, message = "The amount must be greater than 0.")
        @DecimalMax(value = "999999999999999999.99", message = "Quantity value is very large")
        @Digits(integer = 18, fraction = 2)
        @NotNull(message = "Quantity cannot be empty")
        BigDecimal quantity,

        @DecimalMin(value = "0.01", message = "The purchase price must be greater than 0.01.")
        @DecimalMax(value = "999999999999999999.99", message = "Buy price value is very large")
        @Digits(integer = 10, fraction = 2)
        @NotNull(message = "Buy price cannot be empty")
        BigDecimal buyPrice,

        @NotNull(message = "Instrument ID cannot be empty")
        @Positive(message = "Instrument ID cannot be negative")
        Long instrumentsId
) {
}
