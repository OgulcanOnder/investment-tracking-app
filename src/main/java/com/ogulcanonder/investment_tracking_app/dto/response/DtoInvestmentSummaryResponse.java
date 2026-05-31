package com.ogulcanonder.investment_tracking_app.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DtoInvestmentSummaryResponse(
        Long id,
        Long instrumentsId,
        String instrumentsName,
        String imageUrl,
        String type,
        BigDecimal totalQuantity,   // ADET
        BigDecimal averageCost,   // ORTALAMA MALİYET
        BigDecimal currentPrice,    // GÜNCEL FİYAT
        BigDecimal profitLoss,      // KAR / ZARAR
        BigDecimal totalValue
) {
}
