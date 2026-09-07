package com.ogulcanonder.investment_tracking_app.dto.request;


import com.ogulcanonder.investment_tracking_app.enums.DebtType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record DtoDebtRequest(
        @NotNull(message = "Debt Type cannot be blank")
        DebtType debtType,
        @NotBlank(message = "Description cannot be blank")
        @Size(max = 255, message = "Description must be 255 characters long")
        String description,
        @DecimalMin(value = "0.01", message = "The amount price must be greater than 0.01")
        @DecimalMax(value = "999999999999999999.99", message = "Amount value is very large")
        @Digits(integer = 18, fraction = 2)
        @NotNull(message = "Amount cannot be empty")
        BigDecimal amount
) {
}
