package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoResetPasswordRequest;

public interface PasswordResetTokenService {
    public String makeResetToken();

    public void processRequest(String email);

    public void resetPassword(DtoResetPasswordRequest dtoResetPasswordRequest);

    public void deleteExpiredTokens();
}
