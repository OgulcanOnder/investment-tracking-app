package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DtoInstrumentsRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "İmage URL is required") String imageUrl,
        @NotBlank(message = "API symbol is required") String apiSymbol,
        @NotBlank(message = "Type is required") String type) {
}
