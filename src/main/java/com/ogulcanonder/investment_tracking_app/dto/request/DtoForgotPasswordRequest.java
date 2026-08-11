package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DtoForgotPasswordRequest(
        @NotBlank(message = "Email cannot be blank")
        @Size(min = 5, max = 254, message = "Email must be 5-254 characters long")
        @Email(message = "Enter a valid email address")
        String email
) {
}
