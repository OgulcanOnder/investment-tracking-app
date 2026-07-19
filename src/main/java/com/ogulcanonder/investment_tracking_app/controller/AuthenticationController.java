package com.ogulcanonder.investment_tracking_app.controller;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoForgotPasswordRequest;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoLoginRequest;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoRegisterUserRequest;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoResetPasswordRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoAuthLoginResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoUserResponse;
import com.ogulcanonder.investment_tracking_app.service.AuthenticationService;
import com.ogulcanonder.investment_tracking_app.service.PasswordResetTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final PasswordResetTokenService passwordResetTokenService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    PasswordResetTokenService passwordResetTokenService) {
        this.authenticationService = authenticationService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<DtoUserResponse> register(@Valid @RequestBody DtoRegisterUserRequest dtoRegisterUserRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(dtoRegisterUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<DtoAuthLoginResponse> login(@RequestBody DtoLoginRequest dtoLoginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(dtoLoginRequest));
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestHeader("Authorization") String authHeader) {
        return authenticationService.refreshToken(authHeader);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
        return authenticationService.logout(authHeader);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody DtoForgotPasswordRequest dtoForgotPasswordRequest) {
        passwordResetTokenService.processRequest(dtoForgotPasswordRequest.email());
        return ResponseEntity.status(HttpStatus.OK).body("If the email is registered,you will get a reset link");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody DtoResetPasswordRequest dtoResetPasswordRequest) {
        passwordResetTokenService.resetPassword(dtoResetPasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Reset Password Successful");
    }
}
