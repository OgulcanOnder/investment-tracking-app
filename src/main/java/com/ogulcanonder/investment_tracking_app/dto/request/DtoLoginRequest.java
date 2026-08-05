package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DtoLoginRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Enter a valid email address")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
