package com.ogulcanonder.investment_tracking_app.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DtoInstrumentsResponse(Long id, String name, String imageUrl, String type, BigDecimal price) { }