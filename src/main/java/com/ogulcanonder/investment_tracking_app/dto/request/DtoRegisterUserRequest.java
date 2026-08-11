package com.ogulcanonder.investment_tracking_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Locale;

public record DtoRegisterUserRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be 3-50 characters long")
        String name,

        @NotBlank(message = "Surname cannot be blank")
        @Size(min = 3, max = 50, message = "Surname must be 3-50 characters long")
        String surname,

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters long")
        String username,

        @NotBlank(message = "E-mail cannot be blank")
        @Email(message = "Enter a valid e-mail address")
        @Size(min = 5, max = 254, message = "Email must be 5-254 characters long")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 255, message = "Password must be least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*]).*$",
                message = "Password must contain at least one digit, lowercase, uppercase, and special character")
        String password
) {
    public DtoRegisterUserRequest {
        Locale trLocale = new Locale("tr", "TR");
        username = username != null ? username.trim().toLowerCase() : null;
        email = email != null ? email.trim().toLowerCase(Locale.ENGLISH) : null;

        name = name != null ? name.trim().toUpperCase(trLocale) : null;
        surname = surname != null ? surname.trim().toUpperCase(trLocale) : null;
    }
}
