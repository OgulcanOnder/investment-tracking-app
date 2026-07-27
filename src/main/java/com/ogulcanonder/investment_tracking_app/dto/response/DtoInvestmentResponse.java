package com.ogulcanonder.investment_tracking_app.dto.response;

import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DtoInvestmentResponse(Long id, Long userId, BigDecimal quantity, BigDecimal buyPrice,
                                    Instruments instruments) {
}
