package com.ogulcanonder.investment_tracking_app.dto.request;

import com.ogulcanonder.investment_tracking_app.validator.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
@PasswordMatch(
        password = "newPassword",
        passwordConfirm = "confirmNewPassword",
        message = "Confirm new password is incorrect"
)

public record DtoUpdatePasswordRequest(
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 255, message = "Password must be least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*]).*$",
                message = "Password must contain at least one digit, lowercase, uppercase, and special character")
        String oldPassword,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 255, message = "Password must be least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*]).*$",
                message = "Password must contain at least one digit, lowercase, uppercase, and special character")
        String newPassword,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 255, message = "Password must be least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*]).*$",
                message = "Password must contain at least one digit, lowercase, uppercase, and special character")
        String confirmNewPassword
) {
}
