package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoUpdatePasswordRequest;
import com.ogulcanonder.investment_tracking_app.entity.User;

import java.util.Optional;

public interface UserService {
    public Optional<User> findByEmail(String email);

    public void resetPassword(User user, String newPassword);

    public void updatePassword(String email, DtoUpdatePasswordRequest dtoUpdatePasswordRequest);
}
