package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record DtoInstrumentsRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 255, message = "Name must be 3-255 characters long")
        String name,
        @NotBlank(message = "İmage URL is required")
        @Size(min = 19, max = 255, message = "Image URL must be 19-255 characters long")
        @URL(message = "Invalid image URL format")
        String imageUrl,
        @NotBlank(message = "API symbol is required")
        @Size(min = 3, max = 255, message = "API symbol must be 3-255 characters long")
        @Pattern(regexp = "^[A-Z0-9]+$", message = "API symbol must contain only uppercase letters and numbers")
        String apiSymbol,
        @Size(min = 5, max = 255, message = "Type must be 5-255 characters long")
        @NotBlank(message = "Type is required") String type) {
}
