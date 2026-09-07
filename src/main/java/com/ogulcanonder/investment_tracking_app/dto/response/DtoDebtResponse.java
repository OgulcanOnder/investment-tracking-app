package com.ogulcanonder.investment_tracking_app.dto.response;

import com.ogulcanonder.investment_tracking_app.enums.DebtType;

import java.math.BigDecimal;

public record DtoDebtResponse(
        DebtType debtType,
        String description,
        BigDecimal amount
) {
}
