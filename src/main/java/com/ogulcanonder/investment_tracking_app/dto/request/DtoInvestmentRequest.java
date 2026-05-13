package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DtoInvestmentRequest(
        @DecimalMin(value = "0", inclusive = false, message = "The amount must be greater than 0.")
        BigDecimal quantity,
        @DecimalMin(value = "0.01", message = "The purchase price must be greater than 0.01.")
        @Digits(integer = 10, fraction = 2)
        BigDecimal buyPrice,
        Long instrumentsId
) {
}
