package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DtoLoginRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Enter a valid email address")
        @Size(min = 5, max = 254, message = "Email must be 5-254 characters long")
        String email,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 255, message = "Password must be least 8 characters long")
        String password
) {
}
