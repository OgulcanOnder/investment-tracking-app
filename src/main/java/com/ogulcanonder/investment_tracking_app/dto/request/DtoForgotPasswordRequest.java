package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DtoForgotPasswordRequest(
        @NotBlank(message = "Email cannot be blank")
        String email
) {
}
